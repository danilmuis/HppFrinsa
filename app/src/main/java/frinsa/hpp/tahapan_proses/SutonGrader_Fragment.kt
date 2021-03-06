package frinsa.hpp.tahapan_proses

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import frinsa.hpp.R
import frinsa.hpp.data.*
import frinsa.hpp.data.tahap.sutonGrader
import frinsa.hpp.lanjut_produksi.posisi
import frinsa.hpp.lanjut_produksi.spList
import kotlinx.android.synthetic.main.dialog_submit.view.*
import kotlinx.android.synthetic.main.fragment_suton_grader_.*
import kotlinx.android.synthetic.main.fragment_suton_grader_.view.*
import java.text.SimpleDateFormat
import java.util.*

class SutonGrader_Fragment: Fragment(), View.OnClickListener {
    var idData: Int = 0
    var Varietas : String = ""
    var Blok : String = ""
    private val dateFormat = SimpleDateFormat("yyy/MM/dd", Locale.ROOT)

    //Deklarasi semua edit text / textview yg akan divalidasi
    private lateinit var tvTgl: String
    private lateinit var edtBerat: String
    private lateinit var edtOngkosSutonGrading: String
    private lateinit var db : DBPanen
    private lateinit var produk : Produk

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_suton_grader_, container, false)
        view.tv_varietas_suton_grader.text = Varietas
        view.tv_blok_suton_grader.text = Blok
        view.btn_kirim_suton_grader.setOnClickListener(this)
        view.btn_datepicker_suton_grader.setOnClickListener(this)
        db = DBPanen(requireContext())
        produk = Produk(requireContext())

        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_kirim_suton_grader -> {
                val valid = validasiForm()

                if (valid) {
                    val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_submit, null)
                    val builder = AlertDialog.Builder(requireContext()).setView(dialog).setTitle("")
                    val alertDialog =  builder.create()
                    alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnim_Fade
                    alertDialog.show()
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    dialog.submit_submit.setOnClickListener {
                        //INSERT TO DATABASE
                    val sutonGrader = sutonGrader(
                        tanggal = tvTgl,
                        berat = edtBerat.toDouble(),
                        biaya = edtOngkosSutonGrading.toInt()
                    )
                        val success = db.updateStandard<sutonGrader>(
                            idData,
                            sutonGrader,
                            TABLE_SUTON_GRADER,
                            COL_ID_PRODUKSI_SUTON_GRADER,
                            COL_TGL_SUTON_GRADER,
                            COL_BERAT_SUTON_GRADER,
                            COL_BIAYA_SUTON_GRADER)
                        if (success) {
                            val result = db.updateStatus(idData, "suton grader")
                            if (result) {
                                alertDialog.dismiss()
                                activity?.finish()
                            }
                            if (posisi.size > 1) {
                                posisi.forEach {
                                    produk.deleteProduksiById(it)
                                }
                            }

                        }
                    }
                    dialog.batal_submit.setOnClickListener{
                        alertDialog.dismiss()
                    }
                }
            }
            R.id.btn_datepicker_suton_grader -> {
                val now = Calendar.getInstance()
                val datePicker = DatePickerDialog(
                    requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        now.set(Calendar.YEAR, year)
                        now.set(Calendar.MONTH, month)
                        now.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        input_tgl_suton_grader.text = dateFormat.format(now.time)
                    },
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }
        }
    }
    
    fun toastMessage(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun validasiForm(): Boolean {
        var valid: Boolean = false
        //ambil value dari form
        tvTgl = input_tgl_suton_grader.text.toString()
        edtBerat = et_berat_suton_grader.text.toString()
        edtOngkosSutonGrading = et_ongkos_suton_grader.text.toString()

        var isEmptyFields = false

        if (tvTgl == "DD/MM/YYYY") {
            isEmptyFields = true
            input_tgl_suton_grader.setError("Pilih tanggal")
        }

        if (edtBerat.isEmpty()) {
            isEmptyFields = true
            et_berat_suton_grader.error = "Field ini tidak boleh kosong"
        }

        if (edtOngkosSutonGrading.isEmpty()) {
            isEmptyFields = true
            et_ongkos_suton_grader.error = "Field ini tidak boleh kosong"
        }

        if (!isEmptyFields) {
            valid = true
        }
        return valid
    }

}