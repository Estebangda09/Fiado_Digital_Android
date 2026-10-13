package android.fiadodigital

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class SeleccionRegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_registro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAtras = findViewById<ImageView>(R.id.btnAtras)
        btnAtras.setOnClickListener {
            finish()
        }

        val btnNuevoCliente = findViewById<MaterialCardView>(R.id.btnNuevoCliente)
        btnNuevoCliente.setOnClickListener {
            mostrarDialogoRegistrarCliente()
        }

        val btnNuevoAlmacen = findViewById<MaterialCardView>(R.id.btnNuevoAlmacen)
        btnNuevoAlmacen.setOnClickListener {
            mostrarDialogoRegistrarAlmacen()
        }
    }

    private fun mostrarDialogoRegistrarCliente() {
        val vista = LayoutInflater.from(this).inflate(R.layout.dialog_registrar_cliente, null)

        val etNombre = vista.findViewById<TextInputEditText>(R.id.etNombre)
        val etApellido = vista.findViewById<TextInputEditText>(R.id.etApellido)
        val etDocumento = vista.findViewById<TextInputEditText>(R.id.etDocumento)
        val etTelefono = vista.findViewById<TextInputEditText>(R.id.etTelefono)
        val etDireccion = vista.findViewById<TextInputEditText>(R.id.etDireccion)
        val etCorreo = vista.findViewById<TextInputEditText>(R.id.etCorreo)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(vista)
            .setBackground(ContextCompat.getDrawable(this, R.drawable.bg_dialog_registrar_cliente))
            .create()

        vista.findViewById<MaterialButton>(R.id.btnCancelar).setOnClickListener {
            dialog.dismiss()
        }

        vista.findViewById<MaterialButton>(R.id.btnGuardarCliente).setOnClickListener {
            val nombre = etNombre.text?.toString()?.trim().orEmpty()
            val apellido = etApellido.text?.toString()?.trim().orEmpty()
            val documento = etDocumento.text?.toString()?.trim().orEmpty()

            if (nombre.isEmpty()) {
                etNombre.error = "Ingresa el nombre"
                return@setOnClickListener
            }
            if (apellido.isEmpty()) {
                etApellido.error = "Ingresa el apellido"
                return@setOnClickListener
            }
            if (documento.isEmpty()) {
                etDocumento.error = "Ingresa el DNI o CUIT/CUIL"
                return@setOnClickListener
            }

            val telefono = etTelefono.text?.toString()?.trim().orEmpty()
            val direccion = etDireccion.text?.toString()?.trim().orEmpty()
            val correo = etCorreo.text?.toString()?.trim().orEmpty()

            // TODO: guardar los datos del cliente en la base de datos
            Toast.makeText(
                this,
                "Cliente $nombre $apellido registrado",
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun mostrarDialogoRegistrarAlmacen() {
        val vista = LayoutInflater.from(this).inflate(R.layout.dialog_registrar_almacen, null)

        val etNombreAlmacen = vista.findViewById<TextInputEditText>(R.id.etNombreAlmacen)
        val etResponsable = vista.findViewById<TextInputEditText>(R.id.etResponsable)
        val etCuit = vista.findViewById<TextInputEditText>(R.id.etCuit)
        val etDireccion = vista.findViewById<TextInputEditText>(R.id.etDireccionAlmacen)
        val etTelefono = vista.findViewById<TextInputEditText>(R.id.etTelefonoAlmacen)
        val etEmail = vista.findViewById<TextInputEditText>(R.id.etEmailAlmacen)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(vista)
            .setBackground(ContextCompat.getDrawable(this, R.drawable.bg_dialog_registrar_almacen))
            .create()

        vista.findViewById<MaterialButton>(R.id.btnCancelarAlmacen).setOnClickListener {
            dialog.dismiss()
        }

        vista.findViewById<MaterialButton>(R.id.btnGuardarAlmacen).setOnClickListener {
            val nombreAlmacen = etNombreAlmacen.text?.toString()?.trim().orEmpty()
            val responsable = etResponsable.text?.toString()?.trim().orEmpty()
            val cuit = etCuit.text?.toString()?.trim().orEmpty()

            if (nombreAlmacen.isEmpty()) {
                etNombreAlmacen.error = "Ingresa el nombre del almacén"
                return@setOnClickListener
            }
            if (responsable.isEmpty()) {
                etResponsable.error = "Ingresa el nombre del responsable"
                return@setOnClickListener
            }
            if (cuit.isEmpty()) {
                etCuit.error = "Ingresa el CUIT del comercio"
                return@setOnClickListener
            }

            val direccion = etDireccion.text?.toString()?.trim().orEmpty()
            val telefono = etTelefono.text?.toString()?.trim().orEmpty()
            val email = etEmail.text?.toString()?.trim().orEmpty()

            // TODO: guardar los datos del almacén en la base de datos
            Toast.makeText(
                this,
                "Almacén $nombreAlmacen registrado",
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
