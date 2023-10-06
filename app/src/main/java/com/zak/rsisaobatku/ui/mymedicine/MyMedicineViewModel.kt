package com.zak.rsisaobatku.ui.mymedicine

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zak.rsisaobatku.data.local.Medicine
import com.zak.rsisaobatku.data.local.MedicineRepository

class MyMedicineViewModel(application: Application) : ViewModel() {
    private val medicineRepository: MedicineRepository = MedicineRepository(application)

    fun getAllMedicine(): LiveData<List<Medicine>> = medicineRepository.getAllMedicine()

}