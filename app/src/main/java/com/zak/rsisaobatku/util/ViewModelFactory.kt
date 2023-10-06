package com.zak.rsisaobatku.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zak.rsisaobatku.ui.addupdate.AddUpdateViewModel
import com.zak.rsisaobatku.ui.mymedicine.MyMedicineViewModel


class ViewModelFactory(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyMedicineViewModel::class.java)) {
            return MyMedicineViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(AddUpdateViewModel::class.java)) {
            return AddUpdateViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}