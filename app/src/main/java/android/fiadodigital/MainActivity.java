package android.fiadodigital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       TextView tvRegistrarse = findViewById(R.id.tvRegistrarse);

        tvRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SeleccionRegistroActivity.class);


                startActivity(intent);
            }
        });

        View btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: derivar segun el tipo de usuario (cliente / almacen)
                Intent intent = new Intent(MainActivity.this, InicioClienteActivity.class);
                startActivity(intent);
            }
        });

        // DEBUG: accesos directos a cada pantalla
        abrirPantalla(R.id.btnDebugCliente, InicioClienteActivity.class);
        abrirPantalla(R.id.btnDebugAlmacenero, InicioAlmaceneroActivity.class);
        abrirPantalla(R.id.btnDebugRegistro, SeleccionRegistroActivity.class);
    }

    private void abrirPantalla(int idVista, final Class<?> destino) {
        findViewById(idVista).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, destino));
            }
        });
    }
}