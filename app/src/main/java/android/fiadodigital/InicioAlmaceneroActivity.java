package android.fiadodigital;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InicioAlmaceneroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_almacenero);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cargarCuentas();
        cargarMovimientos();
        configurarAccesos();
        configurarNavegacion();
    }

    // ------------------------------------------------------------------
    // Datos de ejemplo (provisorios)
    // ------------------------------------------------------------------
    private static class Cuenta {
        final String cliente;
        final String vencimiento;
        final String saldo;
        final String estado;

        Cuenta(String cliente, String vencimiento, String saldo, String estado) {
            this.cliente = cliente;
            this.vencimiento = vencimiento;
            this.saldo = saldo;
            this.estado = estado;
        }
    }

    private static class Movimiento {
        final boolean esPago;
        final String titulo;
        final String cliente;
        final String importe;
        final String fecha;

        Movimiento(boolean esPago, String titulo, String cliente, String importe, String fecha) {
            this.esPago = esPago;
            this.titulo = titulo;
            this.cliente = cliente;
            this.importe = importe;
            this.fecha = fecha;
        }
    }

    private static class Cliente {
        final String nombre;
        final String documento;

        Cliente(String nombre, String documento) {
            this.nombre = nombre;
            this.documento = documento;
        }
    }

    // TODO: reemplazar por los clientes reales del almacenero
    private List<Cliente> clientesDeEjemplo() {
        List<Cliente> lista = new ArrayList<>();
        lista.add(new Cliente("Juan Pérez", "30.123.456"));
        lista.add(new Cliente("María López", "27.845.102"));
        lista.add(new Cliente("Carlos Ruiz", "33.640.997"));
        lista.add(new Cliente("Ana Gómez", "35.112.480"));
        lista.add(new Cliente("Pedro Sosa", "28.990.331"));
        return lista;
    }

    private void cargarCuentas() {
        LinearLayout contenedor = findViewById(R.id.contenedorCuentas);
        LayoutInflater inflater = LayoutInflater.from(this);

        Cuenta[] cuentas = new Cuenta[]{
                new Cuenta("Juan Pérez", "Vence el 05/11", "$ 12.300", "DEMORA"),
                new Cuenta("María López", "Vence el 12/11", "$ 8.200", "PAGO_PENDIENTE"),
                new Cuenta("Carlos Ruiz", "Vence el 20/11", "$ 4.000", "AL_DIA"),
                new Cuenta("Ana Gómez", "Vence el 15/11", "$ 6.900", "AL_DIA")
        };

        for (Cuenta cuenta : cuentas) {
            View item = inflater.inflate(R.layout.item_cliente_cuenta, contenedor, false);

            ((TextView) item.findViewById(R.id.tvNombre)).setText(cuenta.cliente);
            ((TextView) item.findViewById(R.id.tvVencimiento)).setText(cuenta.vencimiento);
            ((TextView) item.findViewById(R.id.tvSaldo)).setText(cuenta.saldo);

            TextView tvEstado = item.findViewById(R.id.tvEstado);
            tvEstado.setText(textoEstado(cuenta.estado));
            aplicarPill(tvEstado, cuenta.estado);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avisoPendiente("Clientes");
                }
            });

            contenedor.addView(item);
        }
    }

    private void cargarMovimientos() {
        LinearLayout contenedor = findViewById(R.id.contenedorMovimientos);
        LayoutInflater inflater = LayoutInflater.from(this);

        Movimiento[] movimientos = new Movimiento[]{
                new Movimiento(true, "Pago recibido", "María López", "\u2212 $ 2.000", "30/10"),
                new Movimiento(false, "Fiado", "Juan Pérez", "+ $ 3.200", "28/10"),
                new Movimiento(false, "Fiado", "Carlos Ruiz", "+ $ 1.500", "26/10"),
                new Movimiento(true, "Pago recibido", "Ana Gómez", "\u2212 $ 1.000", "22/10")
        };

        for (int i = 0; i < movimientos.length; i++) {
            Movimiento movimiento = movimientos[i];
            View item = inflater.inflate(R.layout.item_movimiento, contenedor, false);

            ((TextView) item.findViewById(R.id.tvTitulo)).setText(movimiento.titulo);
            ((TextView) item.findViewById(R.id.tvComercio)).setText(movimiento.cliente);
            ((TextView) item.findViewById(R.id.tvFecha)).setText(movimiento.fecha);

            TextView tvImporte = item.findViewById(R.id.tvImporte);
            tvImporte.setText(movimiento.importe);

            CardView contenedorIcono = item.findViewById(R.id.iconoContenedor);
            ImageView ivIcono = item.findViewById(R.id.ivIcono);

            if (movimiento.esPago) {
                contenedorIcono.setCardBackgroundColor(color("#E8F5E9"));
                ivIcono.setImageResource(R.drawable.ic_bajada);
                ivIcono.setImageTintList(ColorStateList.valueOf(color("#175940")));
                tvImporte.setTextColor(color("#175940"));
            } else {
                contenedorIcono.setCardBackgroundColor(color("#FFF3E0"));
                ivIcono.setImageResource(R.drawable.ic_subida);
                ivIcono.setImageTintList(ColorStateList.valueOf(color("#E65100")));
                tvImporte.setTextColor(color("#C62828"));
            }

            contenedor.addView(item);

            if (i < movimientos.length - 1) {
                contenedor.addView(crearDivisor());
            }
        }
    }

    private void configurarAccesos() {
        findViewById(R.id.btnNuevoCliente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Nuevo cliente");
            }
        });

        findViewById(R.id.tvVerClientes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Clientes");
            }
        });

        findViewById(R.id.tvVerHistorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Ver historial completo");
            }
        });

        findViewById(R.id.tvVerEstadisticas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Ver estadísticas");
            }
        });
    }

    private void configurarNavegacion() {
        findViewById(R.id.navClientes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Clientes");
            }
        });

        findViewById(R.id.navNuevoFiado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoNuevoFiado();
            }
        });

        findViewById(R.id.navPagos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Pagos");
            }
        });

        findViewById(R.id.navCuenta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Cuenta");
            }
        });
    }

    // ------------------------------------------------------------------
    // Diálogo: registrar compra fiada
    // ------------------------------------------------------------------
    private void mostrarDialogoNuevoFiado() {
        View vista = LayoutInflater.from(this).inflate(R.layout.dialog_registrar_fiado, null);

        final TextInputEditText etBuscar = vista.findViewById(R.id.etBuscarCliente);
        final MaterialCardView cardResultados = vista.findViewById(R.id.cardResultados);
        final LinearLayout contenedorResultados = vista.findViewById(R.id.contenedorResultados);
        final TextView tvSinResultados = vista.findViewById(R.id.tvSinResultados);
        final TextInputEditText etDetalle = vista.findViewById(R.id.etDetalle);
        final TextInputEditText etMonto = vista.findViewById(R.id.etMonto);
        final MaterialSwitch switchVencimiento = vista.findViewById(R.id.switchVencimiento);
        final LinearLayout contenedorVencimiento = vista.findViewById(R.id.contenedorVencimiento);
        final TextInputEditText etFecha = vista.findViewById(R.id.etFechaVencimiento);
        final MaterialAutoCompleteTextView autoInteres = vista.findViewById(R.id.autoInteres);
        final TextView tvSinPorcentajes = vista.findViewById(R.id.tvSinPorcentajes);

        final List<Cliente> clientes = clientesDeEjemplo();
        final String[] porcentajes = cargarPorcentajesInteres();
        final Cliente[] seleccionado = new Cliente[1];

        final AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(vista)
                .setBackground(ContextCompat.getDrawable(this, R.drawable.bg_dialog_registrar_fiado))
                .create();

        // Fecha de vencimiento: por defecto, hoy
        final Calendar calendario = Calendar.getInstance();
        final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "AR"));
        etFecha.setText(formatoFecha.format(calendario.getTime()));

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = new DatePickerDialog(
                        InicioAlmaceneroActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                                calendario.set(anio, mes, dia);
                                etFecha.setText(formatoFecha.format(calendario.getTime()));
                            }
                        },
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        });

        // Interés: solo porcentajes configurados (sin texto libre)
        if (porcentajes.length == 0) {
            autoInteres.setEnabled(false);
            tvSinPorcentajes.setVisibility(View.VISIBLE);
        } else {
            ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, porcentajes);
            autoInteres.setAdapter(adaptador);
        }

        // Mostrar/ocultar campos de vencimiento
        switchVencimiento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contenedorVencimiento.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        // Búsqueda instantánea de clientes
        final boolean[] desdeSeleccion = {false};
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (desdeSeleccion[0]) {
                    desdeSeleccion[0] = false;
                    return;
                }
                seleccionado[0] = null;
                mostrarCoincidencias(s.toString(), clientes, contenedorResultados,
                        cardResultados, tvSinResultados, etBuscar, seleccionado, desdeSeleccion);
            }
        });

        // Cerrar / cancelar
        vista.findViewById(R.id.btnCerrarFiado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        vista.findViewById(R.id.btnCancelarFiado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Registrar fiado
        vista.findViewById(R.id.btnRegistrarFiado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detalle = texto(etDetalle);
                String monto = texto(etMonto);

                if (seleccionado[0] == null) {
                    etBuscar.setError("Seleccioná un cliente de la lista");
                    return;
                }
                if (detalle.isEmpty()) {
                    etDetalle.setError("Ingresá el detalle de la compra");
                    return;
                }
                if (monto.isEmpty()) {
                    etMonto.setError("Ingresá el monto");
                    return;
                }
                double importe;
                try {
                    importe = Double.parseDouble(monto.replace(",", "."));
                } catch (NumberFormatException e) {
                    etMonto.setError("Monto inválido");
                    return;
                }
                if (importe <= 0) {
                    etMonto.setError("El monto debe ser mayor a 0");
                    return;
                }
                if (switchVencimiento.isChecked()) {
                    if (porcentajes.length == 0) {
                        Toast.makeText(InicioAlmaceneroActivity.this,
                                "Agregá porcentajes desde Cuenta", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (texto(etFecha).isEmpty()) {
                        etFecha.setError("Elegí una fecha");
                        return;
                    }
                    if (autoInteres.getText().toString().trim().isEmpty()) {
                        autoInteres.setError("Elegí un interés");
                        return;
                    }
                }

                // TODO: guardar la compra fiada en la base de datos
                Toast.makeText(InicioAlmaceneroActivity.this,
                        "Fiado registrado: " + seleccionado[0].nombre + " · $ " + monto,
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void mostrarCoincidencias(String consulta, List<Cliente> clientes,
                                      LinearLayout contenedor, MaterialCardView card,
                                      TextView tvSinResultados, TextInputEditText etBuscar,
                                      Cliente[] seleccionado, boolean[] desdeSeleccion) {
        contenedor.removeAllViews();
        String q = consulta.trim().toLowerCase();

        if (q.isEmpty()) {
            card.setVisibility(View.GONE);
            tvSinResultados.setVisibility(View.GONE);
            return;
        }

        List<Cliente> coincidencias = new ArrayList<>();
        for (Cliente cliente : clientes) {
            String doc = cliente.documento.replace(".", "");
            if (cliente.nombre.toLowerCase().contains(q) || doc.contains(q)) {
                coincidencias.add(cliente);
            }
        }

        if (coincidencias.isEmpty()) {
            card.setVisibility(View.GONE);
            tvSinResultados.setVisibility(View.VISIBLE);
            return;
        }

        tvSinResultados.setVisibility(View.GONE);
        card.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (final Cliente cliente : coincidencias) {
            View item = inflater.inflate(R.layout.item_resultado_cliente, contenedor, false);
            ((TextView) item.findViewById(R.id.tvIniciales)).setText(iniciales(cliente.nombre));
            ((TextView) item.findViewById(R.id.tvNombre)).setText(cliente.nombre);
            ((TextView) item.findViewById(R.id.tvDocumento)).setText("DNI " + cliente.documento);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seleccionado[0] = cliente;
                    desdeSeleccion[0] = true;
                    etBuscar.setText(cliente.nombre + " · " + cliente.documento);
                    etBuscar.setSelection(etBuscar.getText().length());
                    etBuscar.setError(null);
                    card.setVisibility(View.GONE);
                    tvSinResultados.setVisibility(View.GONE);
                    ocultarTeclado(etBuscar);
                }
            });
            contenedor.addView(item);
        }
    }

    private String iniciales(String nombre) {
        String[] partes = nombre.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < partes.length && i < 2; i++) {
            if (!partes[i].isEmpty()) {
                sb.append(Character.toUpperCase(partes[i].charAt(0)));
            }
        }
        return sb.toString();
    }

    private void ocultarTeclado(View vista) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
        }
    }

    // TODO: tomar los porcentajes de la configuración del almacenero (Cuenta)
    private String[] cargarPorcentajesInteres() {
        return new String[]{"5%", "10%", "15%", "20%"};
    }

    // ------------------------------------------------------------------
    // Utilidades
    // ------------------------------------------------------------------
    private void avisoPendiente(String seccion) {
        Toast.makeText(this, seccion + " (próximamente)", Toast.LENGTH_SHORT).show();
    }

    private String textoEstado(String estado) {
        switch (estado) {
            case "DEMORA":
                return "Con demora";
            case "PAGO_PENDIENTE":
                return "Pago pendiente";
            default:
                return "Al día";
        }
    }

    private void aplicarPill(TextView vista, String estado) {
        String fondo;
        String texto;
        switch (estado) {
            case "DEMORA":
                fondo = "#FDECEA";
                texto = "#C62828";
                break;
            case "PAGO_PENDIENTE":
                fondo = "#FFF3E0";
                texto = "#E65100";
                break;
            default:
                fondo = "#E8F5E9";
                texto = "#175940";
                break;
        }
        ViewCompat.setBackgroundTintList(vista, ColorStateList.valueOf(color(fondo)));
        vista.setTextColor(color(texto));
    }

    private View crearDivisor() {
        View divisor = new View(this);
        int altura = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1f, getResources().getDisplayMetrics());
        divisor.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, altura));
        divisor.setBackgroundColor(color("#EEEEEE"));
        return divisor;
    }

    private int color(String hex) {
        return android.graphics.Color.parseColor(hex);
    }

    private String texto(TextInputEditText campo) {
        return campo.getText() != null ? campo.getText().toString().trim() : "";
    }
}
