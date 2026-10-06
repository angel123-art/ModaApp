package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class CarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val tvTotal = findViewById<TextView>(R.id.tvTotalCarrito)
        tvTotal.text = "Total: S/ 0.00"

        val btnRegresar = findViewById<MaterialButton>(R.id.btnRegresarCarrito)
        btnRegresar.setOnClickListener {
            finish()
        }
    }
}
