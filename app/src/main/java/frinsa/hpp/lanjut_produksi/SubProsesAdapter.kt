package frinsa.hpp.lanjut_produksi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import frinsa.hpp.R
import frinsa.hpp.daftar_produksi.ModelDaftarProduksi
import frinsa.hpp.data.Produk
import kotlinx.android.synthetic.main.cardviewproses.view.*
val posisi: MutableList<Int> = ArrayList()
class SubProsesAdapter(val context: Context?, private val dpList: MutableList<ModelDaftarProduksi>): RecyclerView.Adapter<SubProsesAdapter.cardViewProses>(){

    val produk: Produk = Produk(context!!)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): cardViewProses {
        return cardViewProses(LayoutInflater.from(context).inflate(R.layout.cardviewproses,parent,false))
    }

    override fun getItemCount(): Int = dpList.size

    inner class cardViewProses(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(modelDaftarProses: ModelDaftarProduksi){
            itemView.sp_tgl.text = modelDaftarProses.tanggal
            itemView.sp_blok.text = modelDaftarProses.blok
            itemView.sp_varietas.text = modelDaftarProses.varietas
            itemView.sp_berat.text = modelDaftarProses.berat.toString()  + " Kg"
            itemView.sp_proses.text = modelDaftarProses.proses
            itemView.sp_biaya.text = produk.formatRupiah(modelDaftarProses.biaya!!.toDouble())
            itemView.sp_tahap.text = modelDaftarProses.tahap
            itemView.sp_id.text = modelDaftarProses.id.toString()
        }
    }
    override fun onBindViewHolder(holder: cardViewProses, position: Int) {
        holder.bind(dpList[position])
        posisi.clear()

        holder.itemView.ceklis.setOnClickListener {
            if (it.ceklis.isChecked) {
                posisi.add(holder.position)
            } else {
                posisi.remove(holder.position)
            }
//
//            var stringBuilder = StringBuilder()
//            posisi.forEach {
//                stringBuilder.append(it.toString()).append("\n")
//            }
//            Toast.makeText(context, stringBuilder, Toast.LENGTH_SHORT).show()
        }

    }

}