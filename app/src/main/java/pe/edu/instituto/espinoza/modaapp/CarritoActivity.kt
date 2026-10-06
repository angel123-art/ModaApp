package pe.edu.instituto.espinoza.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CarritoActivity : AppCompatActivity() {

    private lateinit var adapter: CarritoAdapter
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carrito)

        tvTotal = findViewById(
            R.id.tvTotalCarrito
        )

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerCarrito
            )

        adapter = CarritoAdapter(
            CarritoManager.obtenerItems()
        ) { item ->

            CarritoManager.eliminar(
                item.ropa.id
            )

            cargarCarrito()

            Toast.makeText(
                this,
                "Producto eliminado",
                Toast.LENGTH_SHORT
            ).show()
        }

        recycler.layoutManager =
            LinearLayoutManager(this)

        recycler.adapter = adapter

        val btnPedido =
            findViewById<MaterialButton>(
                R.id.btnRealizarPedido
            )

        btnPedido.setOnClickListener {

            if (CarritoManager.estaVacio()) {

                Toast.makeText(
                    this,
                    "El carrito está vacío",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            startActivity(
                Intent(
                    this,
                    PedidoActivity::class.java
                )
            )
        }

        val btnRegresar =
            findViewById<MaterialButton>(
                R.id.btnRegresarCarrito
            )

        btnRegresar.setOnClickListener {
            finish()
        }

        cargarCarrito()
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {
            cargarCarrito()
        }
    }

    private fun cargarCarrito() {

        adapter.notifyDataSetChanged()

        val total =
            CarritoManager.calcularTotal()

        tvTotal.text =
            "Total: S/ %.2f".format(total)
    }
}