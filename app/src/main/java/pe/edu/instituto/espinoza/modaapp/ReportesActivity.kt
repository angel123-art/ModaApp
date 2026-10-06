package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import pe.edu.instituto.espinoza.modaapp.database.DBHelper

class ReportesActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    private lateinit var tvTotalRopa: TextView
    private lateinit var tvTotalClientes: TextView
    private lateinit var tvTotalPedidos: TextView
    private lateinit var tvPedidosPendientes: TextView
    private lateinit var tvPedidosAtendidos: TextView
    private lateinit var tvTotalVentas: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reportes)

        dbHelper = DBHelper(this)

        tvTotalRopa =
            findViewById(R.id.tvTotalRopa)

        tvTotalClientes =
            findViewById(R.id.tvTotalClientes)

        tvTotalPedidos =
            findViewById(R.id.tvTotalPedidos)

        tvPedidosPendientes =
            findViewById(R.id.tvPedidosPendientes)

        tvPedidosAtendidos =
            findViewById(R.id.tvPedidosAtendidos)

        tvTotalVentas =
            findViewById(R.id.tvTotalVentas)

        val btnRegresar =
            findViewById<MaterialButton>(
                R.id.btnRegresarReportes
            )

        btnRegresar.setOnClickListener {
            finish()
        }

        cargarReporte()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized) {
            cargarReporte()
        }
    }

    private fun cargarReporte() {

        val estadisticas =
            dbHelper.obtenerEstadisticas()

        tvTotalRopa.text =
            estadisticas.totalRopa.toString()

        tvTotalClientes.text =
            estadisticas.totalClientes.toString()

        tvTotalPedidos.text =
            estadisticas.totalPedidos.toString()

        tvPedidosPendientes.text =
            "Pendientes: ${estadisticas.pedidosPendientes}"

        tvPedidosAtendidos.text =
            "Atendidos: ${estadisticas.pedidosAtendidos}"

        tvTotalVentas.text =
            "S/ %.2f".format(
                estadisticas.totalVentas
            )
    }
}