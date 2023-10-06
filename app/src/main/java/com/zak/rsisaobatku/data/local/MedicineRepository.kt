package com.zak.rsisaobatku.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MedicineRepository(application: Application) {
    private val mMedicineDao: MedicineDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = MedicineDatabase.getDatabase(application)
        mMedicineDao = db.medicineDao()
    }

    fun getAllMedicine(): LiveData<List<Medicine>> = mMedicineDao.getAllMedicine()

    fun getFutureId(): LiveData<Int> = mMedicineDao.getFutureId()

    fun insert(medicine: Medicine) {
        executorService.execute { mMedicineDao.insertMedicine(medicine) }
    }

    fun delete(medicine: Medicine) {
        executorService.execute { mMedicineDao.deleteMedicine(medicine) }
    }

    fun update(medicine: Medicine) {
        executorService.execute { mMedicineDao.updateMedicine(medicine) }
    }
}