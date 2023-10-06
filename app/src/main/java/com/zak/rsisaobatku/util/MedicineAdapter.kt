package com.zak.rsisaobatku.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zak.rsisaobatku.R
import com.zak.rsisaobatku.data.local.Medicine

class MedicineAdapter(private val listMedicine: ArrayList<Medicine>) :
    RecyclerView.Adapter<MedicineAdapter.MedViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_med_name)
        val tvMedication: TextView = itemView.findViewById(R.id.tv_medication)
        val tvAddDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvType: TextView = itemView.findViewById(R.id.tv_type)
        val tvInstruct: TextView = itemView.findViewById(R.id.tv_instruct)
        val tvFreq: TextView = itemView.findViewById(R.id.tv_freq)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_medicine, parent, false)
        )


    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        holder.tvName.text = listMedicine[position].name
        holder.tvAddDate.text = holder.itemView.context.getString(R.string.add_date, listMedicine[position].date)
        holder.tvMedication.text = holder.itemView.context.getString(R.string.medication_description, listMedicine[position].medication, listMedicine[position].dosage, listMedicine[position].type)
        holder.tvType.text = listMedicine[position].type
        holder.tvInstruct.text = listMedicine[position].instruction
        holder.tvFreq.text = listMedicine[position].frequency
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listMedicine[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listMedicine.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Medicine)
    }
}