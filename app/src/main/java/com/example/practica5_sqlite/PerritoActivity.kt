package com.example.practica5_sqlite

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PerritoActivity : AppCompatActivity() {

    lateinit var imgPerrito : ImageView
    lateinit var tvNombre : TextView
    lateinit var tvRaza : TextView
    lateinit var tvEdad : TextView
    lateinit var tvDescrip : TextView

    private lateinit var database: AppDatabase
    private lateinit var perrito: Perrito
    private lateinit var perritoLiveData: LiveData<Perrito>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perrito)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imgPerrito = findViewById(R.id.imgPerrito)
        tvNombre = findViewById(R.id.tvNombrePerrito)
        tvRaza = findViewById(R.id.tvRazaPerrito)
        tvEdad = findViewById(R.id.tvEdadPerrito)
        tvDescrip = findViewById(R.id.tvDescrip)

        database = AppDatabase.getDatabase(this)

        val idPerrito = intent.getIntExtra("id", 0)

        val imageUri = ImageController.getImageUri(this, idPerrito.toLong())
        imgPerrito.setImageURI(imageUri)

        perritoLiveData = database.perritos().get(idPerrito)

        perritoLiveData.observe(this, Observer {

            perrito = it
            tvNombre.text = perrito.Nombre
            tvRaza.text = perrito.Raza
            tvEdad.text = perrito.Edad
            tvDescrip.text = perrito.Descripcion

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.perrito_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemEditar -> {
                val intent = Intent(this, NewPerritoActivity::class.java)
                intent.putExtra("perrito", perrito)
                startActivityForResult(intent, 49)
            }

            R.id.itemEliminar -> {
                perritoLiveData.removeObservers(this)

                CoroutineScope(Dispatchers.IO).launch {
                    database.perritos().delete(perrito)
                    this@PerritoActivity.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 49 && resultCode == Activity.RESULT_OK -> {
                imgPerrito.setImageURI(data!!.data)
            }
        }
    }
}