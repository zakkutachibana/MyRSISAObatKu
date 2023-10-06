package com.zak.rsisaobatku.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MedicineDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMedicine(note: Medicine)

    @Update
    fun updateMedicine(note: Medicine)

    @Delete
    fun deleteMedicine(note: Medicine)

    @Query("SELECT * from Medicine ORDER BY date")
    fun getAllMedicine(): LiveData<List<Medicine>>

    @Query("SELECT MAX(id)+1 from Medicine")
    fun getFutureId(): LiveData<Int>
}