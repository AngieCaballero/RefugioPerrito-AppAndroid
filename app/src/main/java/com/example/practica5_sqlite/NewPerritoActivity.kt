package com.example.practica5_sqlite

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPerritoActivity : AppCompatActivity() {

    private val SELECT_ACTIVITY = 50
    private var imageUri : Uri? = null

    lateinit var ptNewNombre : EditText
    lateinit var ptNewRaza : EditText
    lateinit var ptNewEdad : EditText
    lateinit var mtNewDescripcion : EditText
    lateinit var btnGuardar : Button
    lateinit var ivImageView: ImageView
    lateinit var btnCancelar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_perrito)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ptNewNombre = findViewById(R.id.ptNewNombre)
        ptNewRaza = findViewById(R.id.ptNewRaza)
        ptNewEdad = findViewById(R.id.ptNewEdad)
        mtNewDescripcion = findViewById(R.id.tmNewDescripcion)
        btnGuardar = findViewById(R.id.btnGuardarNew)
        ivImageView = findViewById(R.id.ivImagenSelector)
        btnCancelar = findViewById(R.id.btnCancelar)

        var id : Int? = null

        if (intent.hasExtra("perrito")){
            val perrito = intent.extras?.getSerializable("perrito") as Perrito

            ptNewNombre.setText(perrito.Nombre)
            ptNewRaza.setText(perrito.Raza)
            ptNewEdad.setText(perrito.Edad)
            mtNewDescripcion.setText(perrito.Descripcion)
            id = perrito.Id

            val imageUri = ImageController.getImageUri(this, id.toLong())
            ivImageView.setImageURI(imageUri)
        }

        val database = AppDatabase.getDatabase(this)



        btnGuardar.setOnClickListener {

            if(ptNewNombre.text.toString() != "" || ptNewRaza.text.toString() != "" || ptNewEdad.text.toString() != "" || mtNewDescripcion.text.toString() != ""){

                val nombre = ptNewNombre.text.toString()
                val raza = ptNewRaza.text.toString()
                val edad = ptNewEdad.text.toString()
                val descripcion = mtNewDescripcion.text.toString()

                val perrito = Perrito(nombre, raza, edad, descripcion, R.drawable.perrito)

                if(id != null){

                    CoroutineScope(Dispatchers.IO).launch {
                        perrito.Id = id
                        database.perritos().update(perrito)

                        imageUri?.let {
                            ImageController.saveImage(this@NewPerritoActivity, id.toLong(), it)
                        }

                        this@NewPerritoActivity.finish()
                    }
                }else {

                    CoroutineScope(Dispatchers.IO).launch {
                        val id = database.perritos().insertAll(perrito)[0]

                        imageUri?.let {
                            val intent = Intent()
                            intent.data = it
                            setResult(Activity.RESULT_OK, intent)
                            ImageController.saveImage(this@NewPerritoActivity, id, it)
                        }

                        this@NewPerritoActivity.finish()

                    }
                }
            }
            else
            {
                val myToast = Toast.makeText(applicationContext,"Llene todos los campos", Toast.LENGTH_SHORT)
                myToast.setGravity(Gravity.LEFT,200,200)
                myToast.show()
            }

        }

        ivImageView.setOnClickListener {
            ImageController.selectPhotoFromGalery(this, SELECT_ACTIVITY)
        }

        btnCancelar.setOnClickListener {

            showDialogYesOrNot()
        }


    }

    private fun Guardar(){

    }

    private fun showDialogYesOrNot(){
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.dialog_one_style_title)
            .setMessage(R.string.dialog_one_style_message)
            .setNegativeButton(R.string.dialog_one_style_negative_btn) { view, _ ->

            }
            .setPositiveButton(R.string.dialog_one_style_positive_btn) { view, _ ->
                this@NewPerritoActivity.finish()
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data

                ivImageView.setImageURI(imageUri)
            }
        }
    }
}