package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ReportesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)

        val btnRegresar = findViewById<MaterialButton>(R.id.btnRegresarReportes)
        btnRegresar.setOnClickListener {
            finish()
        }
    }
}
