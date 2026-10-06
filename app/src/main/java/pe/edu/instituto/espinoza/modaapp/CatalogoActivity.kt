package pe.edu.instituto.espinoza.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class CatalogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        val btnCarrito = findViewById<MaterialButton>(R.id.btnCarrito)
        btnCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        val btnAtras = findViewById<MaterialButton>(R.id.btnAtrasCatalogo)
        btnAtras.setOnClickListener {
            Toast.makeText(this, "Volviendo al inicio...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}
