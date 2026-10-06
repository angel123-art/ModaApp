package pe.edu.instituto.espinoza.modaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.edu.instituto.espinoza.modaapp.model.Ropa

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "modaapp.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE usuario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                usuario TEXT NOT NULL UNIQUE,
                clave TEXT NOT NULL,
                rol TEXT NOT NULL
            )
            """.trimIndent()
        )

        val admin = ContentValues().apply {
            put("nombre", "Administrador")
            put("usuario", "admin")
            put("clave", "1234")
            put("rol", "ADMIN")
        }
        db.insert("usuario", null, admin)

        db.execSQL(
            """
            CREATE TABLE categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE
            )
            """.trimIndent()
        )

        val categorias = listOf("Polos", "Camisas", "Pantalones", "Vestidos", "Casacas", "Accesorios")
        for (categoria in categorias) {
            val values = ContentValues().apply { put("nombre", categoria) }
            db.insert("categoria", null, values)
        }

        db.execSQL(
            """
            CREATE TABLE ropa (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                codigo TEXT NOT NULL UNIQUE,
                nombre TEXT NOT NULL,
                categoria TEXT NOT NULL,
                talla TEXT NOT NULL,
                color TEXT NOT NULL,
                precio REAL NOT NULL,
                stock INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE cliente (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                telefono TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE pedido (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                total REAL NOT NULL,
                estado TEXT NOT NULL DEFAULT 'PENDIENTE',
                FOREIGN KEY(cliente_id) REFERENCES cliente(id)
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE detalle_pedido (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pedido_id INTEGER NOT NULL,
                ropa_id INTEGER NOT NULL,
                cantidad INTEGER NOT NULL,
                precio REAL NOT NULL,
                subtotal REAL NOT NULL,
                FOREIGN KEY(pedido_id) REFERENCES pedido(id),
                FOREIGN KEY(ropa_id) REFERENCES ropa(id)
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS cliente (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    telefono TEXT NOT NULL
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER NOT NULL,
                    fecha TEXT NOT NULL,
                    total REAL NOT NULL,
                    estado TEXT NOT NULL DEFAULT 'PENDIENTE',
                    FOREIGN KEY(cliente_id) REFERENCES cliente(id)
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS detalle_pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pedido_id INTEGER NOT NULL,
                    ropa_id INTEGER NOT NULL,
                    cantidad INTEGER NOT NULL,
                    precio REAL NOT NULL,
                    subtotal REAL NOT NULL,
                    FOREIGN KEY(pedido_id) REFERENCES pedido(id),
                    FOREIGN KEY(ropa_id) REFERENCES ropa(id)
                )
                """.trimIndent()
            )
        }
    }

    fun insertarRopa(ropa: Ropa): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("codigo", ropa.codigo)
            put("nombre", ropa.nombre)
            put("categoria", ropa.categoria)
            put("talla", ropa.talla)
            put("color", ropa.color)
            put("precio", ropa.precio)
            put("stock", ropa.stock)
        }
        return db.insert("ropa", null, values)
    }

    fun obtenerRopa(): MutableList<Ropa> {
        val lista = mutableListOf<Ropa>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ropa ORDER BY id DESC", null)
        while (cursor.moveToNext()) {
            val ropa = Ropa(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                codigo = cursor.getString(cursor.getColumnIndexOrThrow("codigo")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                talla = cursor.getString(cursor.getColumnIndexOrThrow("talla")),
                color = cursor.getString(cursor.getColumnIndexOrThrow("color")),
                precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"))
            )
            lista.add(ropa)
        }
        cursor.close()
        return lista
    }

    fun actualizarRopa(ropa: Ropa): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("codigo", ropa.codigo)
            put("nombre", ropa.nombre)
            put("categoria", ropa.categoria)
            put("talla", ropa.talla)
            put("color", ropa.color)
            put("precio", ropa.precio)
            put("stock", ropa.stock)
        }
        return db.update("ropa", values, "id = ?", arrayOf(ropa.id.toString()))
    }

    fun eliminarRopa(id: Int): Int {
        val db = writableDatabase
        return db.delete("ropa", "id = ?", arrayOf(id.toString()))
    }

    fun existeCodigo(codigo: String, idExcluir: Int? = null): Boolean {
        val db = readableDatabase
        val query = if (idExcluir == null) "SELECT id FROM ropa WHERE codigo = ?" else "SELECT id FROM ropa WHERE codigo = ? AND id != ?"
        val args = if (idExcluir == null) arrayOf(codigo) else arrayOf(codigo, idExcluir.toString())
        val cursor = db.rawQuery(query, args)
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    fun insertarCliente(nombre: String, telefono: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("telefono", telefono)
        }
        return db.insert("cliente", null, values)
    }

    fun insertarPedido(clienteId: Long, total: Double): Long {
        val db = writableDatabase
        val fecha = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        val values = ContentValues().apply {
            put("cliente_id", clienteId)
            put("fecha", fecha)
            put("total", total)
            put("estado", "PENDIENTE")
        }
        return db.insert("pedido", null, values)
    }

    fun insertarDetallePedido(pedidoId: Long, ropaId: Int, cantidad: Int, precio: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("pedido_id", pedidoId)
            put("ropa_id", ropaId)
            put("cantidad", cantidad)
            put("precio", precio)
            put("subtotal", cantidad * precio)
        }
        return db.insert("detalle_pedido", null, values)
    }

    fun descontarStock(ropaId: Int, cantidad: Int): Int {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT stock FROM ropa WHERE id = ?", arrayOf(ropaId.toString()))
        if (!cursor.moveToFirst()) {
            cursor.close()
            return 0
        }
        val stockActual = cursor.getInt(0)
        cursor.close()
        if (stockActual < cantidad) return 0
        val values = ContentValues().apply { put("stock", stockActual - cantidad) }
        return db.update("ropa", values, "id = ?", arrayOf(ropaId.toString()))
    }

    fun validarUsuario(usuario: String, clave: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM usuario WHERE usuario = ? AND clave = ? AND rol = 'ADMIN'",
            arrayOf(usuario, clave)
        )
        val valido = cursor.moveToFirst()
        cursor.close()
        return valido
    }
}
