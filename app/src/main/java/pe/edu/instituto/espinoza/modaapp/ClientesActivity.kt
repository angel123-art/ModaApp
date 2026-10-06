package pe.edu.instituto.espinoza.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.edu.instituto.espinoza.modaapp.database.DBHelper

class ClientesActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: ClientesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_clientes
        )

        dbHelper = DBHelper(this)

        val recycler =
            findViewById<RecyclerView>(
                R.id.recyclerClientes
            )

        adapter = ClientesAdapter(
            dbHelper.obtenerClientes()
        )

        recycler.layoutManager =
            LinearLayoutManager(this)

        recycler.adapter = adapter

        val btnRegresar =
            findViewById<MaterialButton>(
                R.id.btnRegresarClientes
            )

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {

            adapter.actualizarLista(
                dbHelper.obtenerClientes()
            )
        }
    }
}