package android.fiadodigital;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Almacenero",
        indices ={@Index(value ={ "cuit", "numeroCuenta"}, unique = true)}
)

public class Almacenero {
    @PrimaryKey(autoGenerate=true)
    public int idAlmacenero;

    public String nombre;
    public String cuit;
    public String telefono;
    public String contrasena;
    public int numeroCuenta;
    public String qr;
    public String fechaAlta;
    public String fechaBaja;


}
