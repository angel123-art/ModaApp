package pe.edu.instituto.espinoza.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_menu)

        // ==========================================
        // GESTIÓN DE ROPA
        // ==========================================

        val btnRopa =
            findViewById<MaterialButton>(R.id.btnRopa)

        btnRopa.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RopaActivity::class.java
                )
            )
        }


        // ==========================================
        // PEDIDOS
        // ==========================================

        val btnPedidos =
            findViewById<MaterialButton>(R.id.btnPedidos)

        btnPedidos.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PedidosActivity::class.java
                )
            )
        }


        // ==========================================
        // CLIENTES
        // ==========================================

        val btnClientes =
            findViewById<MaterialButton>(R.id.btnClientes)

        btnClientes.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ClientesActivity::class.java
                )
            )
        }


        // ==========================================
        // REPORTES
        // ==========================================

        val btnReportes =
            findViewById<MaterialButton>(R.id.btnReportes)

        btnReportes.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ReportesActivity::class.java
                )
            )
        }


        // ==========================================
        // CERRAR SESIÓN
        // ==========================================

        val btnSalir =
            findViewById<MaterialButton>(R.id.btnSalir)

        btnSalir.setOnClickListener {

            val intent =
                Intent(
                    this,
                    LoginActivity::class.java
                )

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
        }
    }
}