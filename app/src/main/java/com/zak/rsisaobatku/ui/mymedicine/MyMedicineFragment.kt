package com.zak.rsisaobatku.ui.mymedicine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zak.rsisaobatku.MainActivity
import com.zak.rsisaobatku.data.local.Medicine
import com.zak.rsisaobatku.databinding.FragmentMyMedicineBinding
import com.zak.rsisaobatku.ui.addupdate.AddUpdateActivity
import com.zak.rsisaobatku.util.MedicineAdapter
import com.zak.rsisaobatku.util.ViewModelFactory

class MyMedicineFragment : Fragment() {

    private var _binding: FragmentMyMedicineBinding? = null
    private val binding get() = _binding!!
    private lateinit var myMedicineViewModel: MyMedicineViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyMedicineBinding.inflate(inflater, container, false)

        binding.rvMedicine.layoutManager = LinearLayoutManager(activity)
        binding.rvMedicine.setHasFixedSize(true)

        val factory = ViewModelFactory((activity as MainActivity).application)
        myMedicineViewModel = ViewModelProvider(this, factory)[MyMedicineViewModel::class.java]

        setViewModel()

        return binding.root
    }

    private fun setViewModel() {
        myMedicineViewModel.getAllMedicine().observe(viewLifecycleOwner) { medicineList ->
            if (medicineList.isNotEmpty()) {
                setMedicine()
            }
        }
    }

    private fun setMedicine() {
        myMedicineViewModel.getAllMedicine().observe(viewLifecycleOwner) { medicine: List<Medicine>? ->
            val items = arrayListOf<Medicine>()
            if (medicine != null) {
                medicine.map {
                    val item = Medicine(
                        it.id,
                        it.name,
                        it.medication,
                        it.type,
                        it.dosage,
                        it.instruction,
                        it.frequency,
                        it.date
                    )
                    items.add(item)
                }
                val adapter = MedicineAdapter(items)
                binding.rvMedicine.adapter = adapter

                adapter.setOnItemClickCallback(object : MedicineAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Medicine) {
                        showMedicine(data)
                    }
                })
            }
        }
    }
    private fun showMedicine(userItems: Medicine) {
        val objectIntent = Intent(context, AddUpdateActivity::class.java)
        objectIntent.putExtra(AddUpdateActivity.EXTRA_MEDICINE, userItems)
        startActivity(objectIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}