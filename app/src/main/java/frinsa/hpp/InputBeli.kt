package frinsa.hpp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import frinsa.hpp.data.*
import kotlinx.android.synthetic.main.activity_input_beli.*
import kotlinx.android.synthetic.main.activity_input_panen.*
import kotlinx.android.synthetic.main.activity_input_panen.tv_proses
import kotlinx.android.synthetic.main.dialog_submit.view.*
import kotlinx.android.synthetic.main.dialog_tmbh_varietas.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InputBeli : AppCompatActivity(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ROOT)

    private val blok: MutableList<String> = ArrayList()
    private val varietas: MutableList<String> = ArrayList()
    private val proses: MutableList<String> = ArrayList()

    private lateinit var spinVarietas: String
    private lateinit var edtBlok: String
    private lateinit var spinProses: String
    private lateinit var tvTanggal: String
    private lateinit var edtBerat: String
    private lateinit var edtKolektif: String
    private lateinit var edtHargaBeli: String
    private lateinit var edtOngkosCuci: String
    private lateinit var terpilih: String
    private var id: Int = 0

    private var isiNanti: Boolean = false
    private var cek: Boolean = false

    private val context = this
    private lateinit var db: DBPanen
    private lateinit var vari: Varietas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_beli)

        //CREATE DATABASE
        db = DBPanen(context)//CREATE DATABASE
        db = DBPanen(context)
        //CREATE VARIETAS OBJECT
        vari = Varietas(context)

        //set action bar title
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "Beli Dari Kebun Lain"
        }

        //Handling Checkbox
        cb_isi_nanti_beli.setOnClickListener(this)

        //call setSpinner function
        setSpinnerVarietas()
        setSpinnerProses()

        //DatePicker
        btn_datepicker_beli.setOnClickListener(this)

        //Dialog Box
        btn_tmbh_varietas_beli.setOnClickListener(this)
        btn_kirim_beli.setOnClickListener(this)

        //RadioButton
        rg_bentuk_beli.setOnCheckedChangeListener(this)
    }

    fun setSpinnerVarietas() {
        //Spinner Varietas
        val spinnerVarietas: Spinner = findViewById(R.id.spinner_varietas_beli)

        val varietas = vari.getVarietas()
        //Style and populate the spinner
        val adapterVarietas = ArrayAdapter(this, android.R.layout.simple_spinner_item, varietas)
        //Dropdown layout style
        adapterVarietas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Attaching the data to spinner
        spinnerVarietas.adapter = adapterVarietas

        spinnerVarietas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                spinVarietas = parent.getItemAtPosition(position).toString()
                if (parent.getItemAtPosition(position) === "Pilih Varietas" ) {
                    //
                } else {
                    Toast.makeText(parent.context, spinVarietas, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //
            }
        }
    }

    fun setSpinnerProses() {
        //Spinner Proses
        val spinnerProses: Spinner = findViewById(R.id.spinner_proses_beli)
        val listP = db.readProses()
        proses.clear()
        proses.add(0, "Pilih Proses")
        if (listP.size > 0) {
            for (i in 0 until listP.size) {
                proses.add(listP[i].name)
            }
        }
        //Style and populate the spinner
        val adapterProses = ArrayAdapter(this, android.R.layout.simple_spinner_item, proses)
        //Dropdown layout style
        adapterProses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Attaching the data to spinner
        spinnerProses.adapter = adapterProses

        spinnerProses.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                spinProses = parent.getItemAtPosition(position).toString()
                if (parent.getItemAtPosition(position) === "Pilih Proses" ) {
                    //
                } else {
                    Toast.makeText(parent.context, spinProses, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //
            }
        }
    }

    private fun setDisable(a: EditText, b: TextView) {
        a.isEnabled = false
        b.setTextColor(Color.parseColor("#c2a7a9"))
    }

    private fun setEnable(a: EditText, b: TextView) {
        a.isEnabled = true
        b.setTextColor(Color.parseColor("#000000"))
    }

    private fun toastMessage(text: String) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    }

    private fun validationKosong(): Boolean {
        var valid: Boolean = false
        tvTanggal = input_tgl_beli.text.toString()
        edtBlok = et_blok_beli.text.toString()
        edtBerat = et_berat_beli.text.toString()
        edtKolektif = et_kolektif_beli.text.toString()
        edtHargaBeli = et_harga_beli.text.toString()
        edtOngkosCuci = et_ongkos_cuci_beli.text.toString()

        var isEmptyFields = false

        if (id == -1) {
            isEmptyFields = true
            toastMessage("Bentuk beli harus dipilih")
        }

        if (tvTanggal == "DD/MM/YYYY") {
            isEmptyFields = true
            input_tgl.setError("Pilih tanggal")
        }

        if (spinVarietas == "Pilih Varietas") {
            isEmptyFields = true
            toastMessage("Var[ietas tidak boleh kosong")
        }

        if (spinProses == "Pilih Proses" && isiNanti == false) {
            isEmptyFields = true
            toastMessage("Proses tidak boleh kosong")
        }

        if (edtBlok.isEmpty()) {
            isEmptyFields = true
            et_berat.error = "Field ini tidak boleh kosong"
        }

        if (edtBerat.isEmpty()) {
            isEmptyFields = true
            et_berat.error = "Field ini tidak boleh kosong"
        }

        if (edtHargaBeli.isEmpty()) {
            isEmptyFields = true
            et_ongkos_petik.error = "Field ini tidak boleh kosong"
        }

        if (edtOngkosCuci.isEmpty() && !cek) {
            isEmptyFields = true
            et_ojek.error = "Field ini tidak boleh kosong"
        }

        if (!isEmptyFields) {
            valid = true
        }
        return valid
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.btn_datepicker_beli -> {
                val now = Calendar.getInstance()
                val datePicker = DatePickerDialog(
                    this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        now.set(Calendar.YEAR, year)
                        now.set(Calendar.MONTH, month)
                        now.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        input_tgl_beli.text = dateFormat.format(now.time)
                    },
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }
            R.id.btn_tmbh_varietas_beli -> {
                val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_tmbh_varietas, null)
                val builder = AlertDialog.Builder(this).setView(dialog).setTitle("Tambah Varietas")
                val alertDialog =  builder.show()

                dialog.submit_tmbh_varietas.setOnClickListener{
                    val edtTambahVarietas: EditText = dialog.edt_dialog_tmbh_varietas
                    val inputTambahVarietas = edtTambahVarietas.text.toString()

                    if (inputTambahVarietas.isEmpty()) {
                        edtTambahVarietas.error = "Field ini tidak boleh kosong"
                    }else {
                        val vari =
                            Varietas(name = inputTambahVarietas)
                        db.insertVarietas(vari)
                        setSpinnerVarietas()
                        alertDialog.dismiss()
                    }
                }
                dialog.batal_tmbh_varietas.setOnClickListener{
                    alertDialog.dismiss()
                }
            }
            R.id.cb_isi_nanti_beli -> {
                if (cb_isi_nanti.isChecked) {
                    isiNanti = true
                    spinner_proses.isEnabled = false
                    tv_proses.setTextColor(Color.parseColor("#c2a7a9"))
                } else {
                    isiNanti = false
                    spinner_proses.isEnabled = true
                    tv_proses.setTextColor(Color.parseColor("#000000"))
                }
            }
            R.id.btn_kirim_beli -> {
                //Validasi inputan kosong
                val valid = validationKosong()
//                println1(valid)

                if (valid) {
                    val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_submit, null)
                    val builder = AlertDialog.Builder(this).setView(dialog).setTitle("")
                    val alertDialog =  builder.show()

                    dialog.submit_submit.setOnClickListener {
                        val proses = if (isiNanti) "-" else spinProses
                        val ongkosCuci = if (edtOngkosCuci.isEmpty()) 0 else edtOngkosCuci.toInt()
                        val biaya = edtHargaBeli.toInt() + ongkosCuci

                        //INSERT TO DATABASE

                        //test getData

                        //Intent menggunakan putextra
                        val intent = Intent(this@InputBeli, ReviewHasilBeli::class.java)
                        intent.putExtra(ReviewHasilBeli.EXTRA_TITLE, terpilih)
                        intent.putExtra(ReviewHasilBeli.EXTRA_TGL, tvTanggal)
                        intent.putExtra(ReviewHasilBeli.EXTRA_VARIETAS, spinVarietas)
                        intent.putExtra(ReviewHasilBeli.EXTRA_BLOK, edtBlok)
                        intent.putExtra(ReviewHasilBeli.EXTRA_BERAT, edtBerat)
                        intent.putExtra(ReviewHasilBeli.EXTRA_KOLEKTIF, edtKolektif)
                        intent.putExtra(ReviewHasilBeli.EXTRA_BIAYA, biaya.toString())
                        intent.putExtra(ReviewHasilBeli.EXTRA_ONGKOS_PETIK, edtHargaBeli)
                        intent.putExtra(ReviewHasilBeli.EXTRA_ONGKOS_CUCI, ongkosCuci.toString())
                        intent.putExtra(ReviewHasilBeli.EXTRA_PROSES, proses)
                        startActivity(intent)
                        finish()

                        alertDialog.dismiss()
                    }
                    dialog.batal_submit.setOnClickListener{
                        alertDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        id = rg_bentuk_beli.checkedRadioButtonId

        if (id != -1) {
            val radio: RadioButton = findViewById(id)
            terpilih = radio.text.toString()
            toastMessage("$terpilih terpilih")

            if (terpilih == "Gabah" || terpilih == "Asalan") {
                setDisable(et_ongkos_cuci_beli, tv_cuci_beli)
                cek = true
                println(cek)
            }
            else {
                setEnable(et_ongkos_cuci_beli, tv_cuci_beli)
            }
        }
    }
}