package pe.edu.instituto.espinoza.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import pe.edu.instituto.espinoza.modaapp.database.DBHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val etUsuario = findViewById<TextInputEditText>(R.id.etUsuario)
        val etContrasena = findViewById<TextInputEditText>(R.id.etContrasena)
        val btnIngresar = findViewById<MaterialButton>(R.id.btnIngresar)
        val btnCatalogo = findViewById<MaterialButton>(R.id.btnCatalogo)

        // ==========================================
        // LOGIN ADMINISTRADOR
        // ==========================================
        btnIngresar.setOnClickListener {

            val usuario = etUsuario.text.toString().trim()
            val clave = etContrasena.text.toString().trim()

            if (usuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(
                    this,
                    "Complete usuario y contraseña",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val valido = dbHelper.validarUsuario(usuario, clave)

            if (valido) {

                Toast.makeText(
                    this,
                    "Bienvenido administrador",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this,
                        MenuActivity::class.java
                    )
                )

            } else {

                Toast.makeText(
                    this,
                    "Usuario o contraseña incorrectos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // ==========================================
        // ACCESO AL CATÁLOGO SIN LOGIN
        // ==========================================
        btnCatalogo.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CatalogoActivity::class.java
                )
            )
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}