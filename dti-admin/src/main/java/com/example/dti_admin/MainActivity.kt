package com.example.dti_admin

import AdminInfoFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView =findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.admin_bottom_notifications -> {
                    replaceFragment(AdminNotificationsFragment())
                    true
                }
                R.id.admin_bottom_manage -> {
                    replaceFragment(AdminManageFragment())
                    true
                }
                R.id.admin_bottom_shareinfo -> {
                    replaceFragment(AdminInfoFragment())
                    true
                }
                R.id.admin_bottom_account -> {
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