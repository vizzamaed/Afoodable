package com.example.afoodableadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        bottomNavigationView =findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.admin_notifications -> {
                    replaceFragment(AdminNotificationsFragment())
                    true
                }
                R.id.admin_manage -> {
                    replaceFragment(AdminManageFragment())
                    true
                }
                R.id.admin_info -> {
                    replaceFragment(AdminInfoFragment())
                    true
                }
                R.id.admin_account -> {
                    replaceFragment(AdminAccountFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(AdminNotificationsFragment())
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }
}
    }
}