package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class PedidosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        val btnRegresar = findViewById<MaterialButton>(R.id.btnRegresarPedidos)
        btnRegresar.setOnClickListener {
            finish()
        }
    }
}
