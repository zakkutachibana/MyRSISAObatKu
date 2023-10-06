package com.zak.rsisaobatku.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "medication")
    var medication: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "dosage")
    var dosage: String? = null,

    @ColumnInfo(name = "instruction")
    var instruction: String? = null,

    @ColumnInfo(name = "frequency")
    var frequency: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null
) : Parcelable