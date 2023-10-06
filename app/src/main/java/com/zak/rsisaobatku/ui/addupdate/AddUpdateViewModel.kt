package com.zak.rsisaobatku.ui.addupdate

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zak.rsisaobatku.data.local.Medicine
import com.zak.rsisaobatku.data.local.MedicineRepository

class AddUpdateViewModel (application: Application) : ViewModel() {
    private val medicineRepository: MedicineRepository = MedicineRepository(application)

    fun insert(medicine: Medicine) {
        medicineRepository.insert(medicine)
    }

    fun update(medicine: Medicine) {
        medicineRepository.update(medicine)
    }

    fun delete(medicine: Medicine) {
        medicineRepository.delete(medicine)
    }

    fun getFutureId(): LiveData<Int> = medicineRepository.getFutureId()

}