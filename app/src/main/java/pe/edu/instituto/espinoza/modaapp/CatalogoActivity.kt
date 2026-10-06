package pe.edu.instituto.espinoza.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.edu.instituto.espinoza.modaapp.database.DBHelper

class CatalogoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: CatalogoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        dbHelper = DBHelper(this)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCatalogo)
        adapter = CatalogoAdapter(dbHelper.obtenerRopa()) { ropa ->
            Toast.makeText(this, " seleccionado", Toast.LENGTH_SHORT).show()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        val btnCarrito = findViewById<MaterialButton>(R.id.btnCarrito)
        btnCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        val btnAtras = findViewById<MaterialButton>(R.id.btnAtrasCatalogo)
        btnAtras.setOnClickListener {
            Toast.makeText(this, "Volviendo al inicio...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.actualizarLista(dbHelper.obtenerRopa())
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
