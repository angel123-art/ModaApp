package pe.edu.instituto.espinoza.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<TextInputEditText>(R.id.etUsuario)
        val etContrasena = findViewById<TextInputEditText>(R.id.etContrasena)
        val btnIngresar = findViewById<MaterialButton>(R.id.btnIngresar)
        val btnCatalogo = findViewById<MaterialButton>(R.id.btnCatalogo)

        // ==========================================
        // LOGIN ADMINISTRADOR (Credenciales en memoria)
        // ==========================================
        btnIngresar.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val clave = etContrasena.text.toString().trim()

            if (usuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(
                    this,
                    "Complete usuario y contraseÃ±a",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (usuario == "admin" && clave == "1234") {
                Toast.makeText(
                    this,
                    "Bienvenido administrador",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(this, MenuActivity::class.java)
                )
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Credenciales incorrectas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // ==========================================
        // ACCESO AL CATÃLOGO SIN LOGIN
        // ==========================================
        btnCatalogo.setOnClickListener {
            startActivity(
                Intent(this, CatalogoActivity::class.java)
            )
        }
    }
}
