package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class RopaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ropa)

        val btnRegresar = findViewById<MaterialButton>(R.id.btnRegresarRopa)
        btnRegresar.setOnClickListener {
            finish()
        }
    }
}
