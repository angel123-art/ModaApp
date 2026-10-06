package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.edu.instituto.espinoza.modaapp.database.DBHelper

class PedidosActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: PedidosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pedidos)

        dbHelper = DBHelper(this)

        val recyclerPedidos =
            findViewById<RecyclerView>(R.id.recyclerPedidos)

        val btnRegresar =
            findViewById<MaterialButton>(R.id.btnRegresarPedidos)

        recyclerPedidos.layoutManager =
            LinearLayoutManager(this)

        adapter = PedidosAdapter(
            mutableListOf()
        ) { pedido ->

            dbHelper.actualizarEstadoPedido(
                pedido.id,
                "ATENDIDO"
            )

            cargarPedidos()
        }

        recyclerPedidos.adapter = adapter

        btnRegresar.setOnClickListener {
            finish()
        }

        cargarPedidos()
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            cargarPedidos()
        }
    }

    private fun cargarPedidos() {

        val pedidos = dbHelper.obtenerPedidos()

        adapter.actualizarLista(pedidos)
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}