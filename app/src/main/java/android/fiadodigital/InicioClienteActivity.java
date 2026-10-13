package android.fiadodigital;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InicioClienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_cliente);

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
        final String nombre;
        final String vencimiento;
        final String saldo;
        final String estado;

        Cuenta(String nombre, String vencimiento, String saldo, String estado) {
            this.nombre = nombre;
            this.vencimiento = vencimiento;
            this.saldo = saldo;
            this.estado = estado;
        }
    }

    private static class Movimiento {
        final boolean esPago;
        final String titulo;
        final String comercio;
        final String importe;
        final String fecha;

        Movimiento(boolean esPago, String titulo, String comercio, String importe, String fecha) {
            this.esPago = esPago;
            this.titulo = titulo;
            this.comercio = comercio;
            this.importe = importe;
            this.fecha = fecha;
        }
    }

    private void cargarCuentas() {
        LinearLayout contenedor = findViewById(R.id.contenedorCuentas);
        LayoutInflater inflater = LayoutInflater.from(this);

        Cuenta[] cuentas = new Cuenta[]{
                new Cuenta("Almacén Don José", "Vence el 05/11", "$ 12.300", "VENCIDA"),
                new Cuenta("Kiosco La Esquina", "Vence el 12/11", "$ 8.200", "AL_DIA"),
                new Cuenta("Almacén Doña Rosa", "Sin cuotas", "$ 4.000", "SIN_DEUDA")
        };

        for (Cuenta cuenta : cuentas) {
            View item = inflater.inflate(R.layout.item_cuenta_almacen, contenedor, false);

            ((TextView) item.findViewById(R.id.tvNombre)).setText(cuenta.nombre);
            ((TextView) item.findViewById(R.id.tvVencimiento)).setText(cuenta.vencimiento);
            ((TextView) item.findViewById(R.id.tvSaldo)).setText(cuenta.saldo);

            TextView tvEstado = item.findViewById(R.id.tvEstado);
            tvEstado.setText(textoEstado(cuenta.estado));
            aplicarPill(tvEstado, cuenta.estado);

            contenedor.addView(item);
        }
    }

    private void cargarMovimientos() {
        LinearLayout contenedor = findViewById(R.id.contenedorMovimientos);
        LayoutInflater inflater = LayoutInflater.from(this);

        Movimiento[] movimientos = new Movimiento[]{
                new Movimiento(true, "Pago informado", "Almacén Don José", "\u2212 $ 2.000", "30/10"),
                new Movimiento(false, "Compra fiada", "Almacén Don José", "+ $ 3.200", "28/10"),
                new Movimiento(false, "Compra fiada", "Kiosco La Esquina", "+ $ 1.500", "26/10"),
                new Movimiento(true, "Pago informado", "Kiosco La Esquina", "\u2212 $ 1.000", "22/10")
        };

        for (int i = 0; i < movimientos.length; i++) {
            Movimiento movimiento = movimientos[i];
            View item = inflater.inflate(R.layout.item_movimiento, contenedor, false);

            ((TextView) item.findViewById(R.id.tvTitulo)).setText(movimiento.titulo);
            ((TextView) item.findViewById(R.id.tvComercio)).setText(movimiento.comercio);
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
        findViewById(R.id.btnVincularAlmacen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Vincular almacén");
            }
        });

        findViewById(R.id.btnInformarPago).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Informar un pago");
            }
        });

        findViewById(R.id.tvVerAlmacenes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Ver todos los almacenes");
            }
        });

        findViewById(R.id.tvVerHistorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Ver historial completo");
            }
        });
    }

    private void configurarNavegacion() {
        findViewById(R.id.navAlmacenes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Almacenes");
            }
        });

        findViewById(R.id.navInformarPago).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avisoPendiente("Informar pago");
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
    // Utilidades
    // ------------------------------------------------------------------
    private void avisoPendiente(String seccion) {
        Toast.makeText(this, seccion + " (próximamente)", Toast.LENGTH_SHORT).show();
    }

    private String textoEstado(String estado) {
        switch (estado) {
            case "VENCIDA":
                return "Vencida";
            case "SIN_DEUDA":
                return "Sin deuda";
            default:
                return "Al día";
        }
    }

    private void aplicarPill(TextView vista, String estado) {
        String fondo;
        String texto;
        switch (estado) {
            case "VENCIDA":
                fondo = "#FDECEA";
                texto = "#C62828";
                break;
            case "SIN_DEUDA":
                fondo = "#EEEEEE";
                texto = "#616161";
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
}
