package com.zak.rsisaobatku

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.zak.rsisaobatku.databinding.ActivityMainBinding
import com.zak.rsisaobatku.ui.addupdate.AddUpdateActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO 6: Buat bottom navigation yang lebih cakep, pindahin FAB ke main activity kayanya
        //TODO 7: Menu dibuat 2 aja kah? Isinya daftar obat, sama berita/artikel tentang obat
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.background = null
        navView.menu.getItem(1).isEnabled = false

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_my_medicine, R.id.navigation_dashboard, R.id.navigation_news
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddUpdateActivity::class.java)
            startActivity(intent)
        }
    }
}