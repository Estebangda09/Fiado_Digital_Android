package android.fiadodigital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class SeleccionRegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seleccion_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // flecha ir atras
        ImageView btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // boton nuevo cliente
        MaterialCardView btnNuevoCliente = findViewById(R.id.btnNuevoCliente);
        btnNuevoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoRegistrarCliente();
            }
        });

        // boton nuevo almacen
        MaterialCardView btnNuevoAlmacen = findViewById(R.id.btnNuevoAlmacen);
        btnNuevoAlmacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoRegistrarAlmacen();
            }
        });
    }

    private void mostrarDialogoRegistrarCliente() {
        View vista = LayoutInflater.from(this).inflate(R.layout.dialog_registrar_cliente, null);

        TextInputEditText etNombre = vista.findViewById(R.id.etNombre);
        TextInputEditText etApellido = vista.findViewById(R.id.etApellido);
        TextInputEditText etDocumento = vista.findViewById(R.id.etDocumento);
        TextInputEditText etTelefono = vista.findViewById(R.id.etTelefono);
        TextInputEditText etDireccion = vista.findViewById(R.id.etDireccion);
        TextInputEditText etCorreo = vista.findViewById(R.id.etCorreo);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(vista)
                .setBackground(ContextCompat.getDrawable(this, R.drawable.bg_dialog_registrar_cliente))
                .create();

        vista.findViewById(R.id.btnCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        vista.findViewById(R.id.btnGuardarCliente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = texto(etNombre);
                String apellido = texto(etApellido);
                String documento = texto(etDocumento);

                if (nombre.isEmpty()) {
                    etNombre.setError("Ingresa el nombre");
                    return;
                }
                if (apellido.isEmpty()) {
                    etApellido.setError("Ingresa el apellido");
                    return;
                }
                if (documento.isEmpty()) {
                    etDocumento.setError("Ingresa el DNI o CUIT/CUIL");
                    return;
                }

                String telefono = texto(etTelefono);
                String direccion = texto(etDireccion);
                String correo = texto(etCorreo);

                // TODO: guardar los datos del cliente en la base de datos
                Toast.makeText(
                        SeleccionRegistroActivity.this,
                        "Cliente " + nombre + " " + apellido + " registrado",
                        Toast.LENGTH_SHORT
                ).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void mostrarDialogoRegistrarAlmacen() {
        View vista = LayoutInflater.from(this).inflate(R.layout.dialog_registrar_almacen, null);

        TextInputEditText etNombreAlmacen = vista.findViewById(R.id.etNombreAlmacen);
        TextInputEditText etResponsable = vista.findViewById(R.id.etResponsable);
        TextInputEditText etCuit = vista.findViewById(R.id.etCuit);
        TextInputEditText etDireccion = vista.findViewById(R.id.etDireccionAlmacen);
        TextInputEditText etTelefono = vista.findViewById(R.id.etTelefonoAlmacen);
        TextInputEditText etEmail = vista.findViewById(R.id.etEmailAlmacen);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(vista)
                .setBackground(ContextCompat.getDrawable(this, R.drawable.bg_dialog_registrar_almacen))
                .create();

        vista.findViewById(R.id.btnCancelarAlmacen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        vista.findViewById(R.id.btnGuardarAlmacen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreAlmacen = texto(etNombreAlmacen);
                String responsable = texto(etResponsable);
                String cuit = texto(etCuit);

                if (nombreAlmacen.isEmpty()) {
                    etNombreAlmacen.setError("Ingresa el nombre del almacén");
                    return;
                }
                if (responsable.isEmpty()) {
                    etResponsable.setError("Ingresa el nombre del responsable");
                    return;
                }
                if (cuit.isEmpty()) {
                    etCuit.setError("Ingresa el CUIT del comercio");
                    return;
                }

                String direccion = texto(etDireccion);
                String telefono = texto(etTelefono);
                String email = texto(etEmail);

                // TODO: guardar los datos del almacén en la base de datos
                Toast.makeText(
                        SeleccionRegistroActivity.this,
                        "Almacén " + nombreAlmacen + " registrado",
                        Toast.LENGTH_SHORT
                ).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String texto(TextInputEditText campo) {
        return campo.getText() != null ? campo.getText().toString().trim() : "";
    }
}
