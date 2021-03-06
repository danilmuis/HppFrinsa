package frinsa.hpp.data

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import frinsa.hpp.R
import kotlinx.android.synthetic.main.dialog_tmbh_varietas.view.*


open class Varietas {
    private val varietas: MutableList<String> = ArrayList()
    var id: Int = 0
    var name: String = ""
    lateinit var context: Context
    private lateinit var db: DBPanen

    constructor(name: String) {
        this.name = name
    }
    constructor(context: Context){
        this.context = context
        db = DBPanen(this.context)
    }
    constructor(){}

    fun addVarietas() {
        val dialog = LayoutInflater.from(context).inflate(R.layout.dialog_tmbh_varietas, null)
        val builder = AlertDialog.Builder(context).setView(dialog).setTitle("Tambah Varietas")
        val alertDialog =  builder.create()
        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnim_Up_Down
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.edt_dialog_tmbh_varietas.requestFocus()


        dialog.submit_tmbh_varietas.setOnClickListener{
            val edtTambahVarietas: EditText = dialog.edt_dialog_tmbh_varietas
            val inputTambahVarietas = edtTambahVarietas.text.toString().capitalizeWords()

            if (inputTambahVarietas.isEmpty()) {
                edtTambahVarietas.error = "Field ini tidak boleh kosong"
            }else {
                val vari =
                    Varietas(name = inputTambahVarietas)
//                db.insertVarietas(vari)
                db.insertSpin<Varietas>(vari, TABLE_VARIETAS)
                alertDialog.dismiss()
            }
        }
        dialog.batal_tmbh_varietas.setOnClickListener{
            alertDialog.dismiss()
        }
    }

    fun getVarietas(): MutableList<String> {
        val listV = db.readSpin(TABLE_VARIETAS)
        varietas.clear()
        varietas.add(0, "Pilih Varietas")
        if (listV.size > 0) {
            for (i in 0 until listV.size) {
                varietas.add(listV[i].name)
            }
        }
        return varietas
    }

    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")
}