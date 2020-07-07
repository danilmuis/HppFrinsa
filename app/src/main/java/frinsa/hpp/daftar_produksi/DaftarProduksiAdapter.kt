package frinsa.hpp.daftar_produksi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import frinsa.hpp.R
import frinsa.hpp.data.DBPanen
import frinsa.hpp.data.Produk
import frinsa.hpp.data.TABLE_PRODUKSI
import kotlinx.android.synthetic.main.card_daftar_produksi.view.*
import kotlinx.android.synthetic.main.cardviewproses.view.*

//2nd Adapter bagian Recycler View
class DaftarProduksiAdapter (val context: Context?, private val dpList: MutableList<ModelDaftarProduksi>): RecyclerView.Adapter<DaftarProduksiAdapter.cardViewHolder>(){
    val produk: Produk = Produk(context!!)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): cardViewHolder {
        return cardViewHolder(LayoutInflater.from(context).inflate(R.layout.card_daftar_produksi,parent,false))
    }

    override fun getItemCount(): Int = dpList.size

    override fun onBindViewHolder(holder: cardViewHolder, position: Int) {
        holder.bind(dpList[position])

        holder.itemView.btn_dp_delete.setOnClickListener{
            produk.deleteProduksiById(dpList.get(holder.position).id!!.toInt())
        }
    }

    inner class cardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(modelDaftarProduksi: ModelDaftarProduksi){
            itemView.dp_tv_tgl.text = modelDaftarProduksi.tanggal
            itemView.dp_tv_blok.text = modelDaftarProduksi.blok
            itemView.dp_tv_varietas.text = modelDaftarProduksi.varietas
            itemView.dp_tv_berat.text = modelDaftarProduksi.berat.toString() + " Kg"
            itemView.dp_tv_proses.text = modelDaftarProduksi.proses
            itemView.dp_tv_biaya.text = produk.formatRupiah(modelDaftarProduksi.biaya!!.toDouble())
            itemView.dp_tv_tahap.text = modelDaftarProduksi.tahap
            itemView.dp_id.text = modelDaftarProduksi.id.toString()
        }
    }
}