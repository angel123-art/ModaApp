package pe.edu.instituto.espinoza.modaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pe.edu.instituto.espinoza.modaapp.model.Ropa

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "modaapp.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // ==========================================
        // USUARIOS
        // ==========================================
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

        // Administrador inicial
        val admin = ContentValues().apply {
            put("nombre", "Administrador")
            put("usuario", "admin")
            put("clave", "1234")
            put("rol", "ADMIN")
        }
        db.insert("usuario", null, admin)

        // ==========================================
        // CATEGORÃAS
        // ==========================================
        db.execSQL(
            """
            CREATE TABLE categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE
            )
            """.trimIndent()
        )

        val categorias = listOf(
            "Polos",
            "Camisas",
            "Pantalones",
            "Vestidos",
            "Casacas",
            "Accesorios"
        )

        for (categoria in categorias) {
            val values = ContentValues().apply {
                put("nombre", categoria)
            }
            db.insert("categoria", null, values)
        }

        // ==========================================
        // ROPA
        // ==========================================
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // No upgrade needed in v1
    }

    // ==========================================
    // INSERTAR ROPA
    // ==========================================
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

    // ==========================================
    // LISTAR ROPA
    // ==========================================
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

    // ==========================================
    // VALIDAR USUARIO
    // ==========================================
    fun validarUsuario(usuario: String, clave: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id FROM usuario
            WHERE usuario = ? AND clave = ? AND rol = 'ADMIN'
            """.trimIndent(),
            arrayOf(usuario, clave)
        )
        val valido = cursor.moveToFirst()
        cursor.close()
        return valido
    }
}
