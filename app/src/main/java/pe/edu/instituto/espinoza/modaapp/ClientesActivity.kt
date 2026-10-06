package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ClientesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        val btnRegresar = findViewById<MaterialButton>(R.id.btnRegresarClientes)
        btnRegresar.setOnClickListener {
            finish()
        }
    }
}
