package pe.edu.instituto.espinoza.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import pe.edu.instituto.espinoza.modaapp.database.DBHelper
import pe.edu.instituto.espinoza.modaapp.model.Ropa

class CatalogoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: CatalogoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_catalogo)

        dbHelper = DBHelper(this)

        // ==========================================
        // RECYCLER DEL CATÁLOGO
        // ==========================================

        val recycler =
            findViewById<RecyclerView>(R.id.recyclerCatalogo)

        adapter = CatalogoAdapter(
            dbHelper.obtenerRopa()
        ) { ropa ->
            mostrarProducto(ropa)
        }

        recycler.layoutManager =
            LinearLayoutManager(this)

        recycler.adapter = adapter

        // ==========================================
        // BOTÓN CARRITO
        // ==========================================

        val btnCarrito =
            findViewById<MaterialButton>(R.id.btnCarrito)

        btnCarrito.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CarritoActivity::class.java
                )
            )
        }

        // ==========================================
        // BOTÓN ATRÁS - VOLVER AL LOGIN
        // ==========================================

        // ==========================================
// BOTÓN ATRÁS - VOLVER AL LOGIN
// ==========================================

        val btnAtras =
            findViewById<MaterialButton>(R.id.btnAtrasCatalogo)

        btnAtras.setOnClickListener {

            Toast.makeText(
                this,
                "Volviendo al inicio...",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(
                this@CatalogoActivity,
                LoginActivity::class.java
            )

            startActivity(intent)

            finishAffinity()
        }
    }

    // ==========================================
    // ACTUALIZAR CATÁLOGO
    // ==========================================

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {

            adapter.actualizarLista(
                dbHelper.obtenerRopa()
            )
        }
    }

    // ==========================================
    // MOSTRAR PRODUCTO
    // ==========================================

    private fun mostrarProducto(ropa: Ropa) {

        if (ropa.stock <= 0) {

            Toast.makeText(
                this,
                "Este producto no tiene stock",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val opciones = arrayOf(
            "Agregar 1 unidad",
            "Agregar 2 unidades",
            "Agregar 3 unidades"
        )

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(
                "${ropa.nombre} - S/ %.2f".format(
                    ropa.precio
                )
            )
            .setSingleChoiceItems(
                opciones,
                -1
            ) { dialog, which ->

                val cantidad = which + 1

                if (cantidad > ropa.stock) {

                    Toast.makeText(
                        this,
                        "No hay suficiente stock",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setSingleChoiceItems
                }

                CarritoManager.agregar(
                    ropa,
                    cantidad
                )

                Toast.makeText(
                    this,
                    "$cantidad unidad(es) agregada(s) al carrito",
                    Toast.LENGTH_SHORT
                ).show()

                dialog.dismiss()
            }
            .show()
    }

    // ==========================================
    // CERRAR BASE DE DATOS
    // ==========================================

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}