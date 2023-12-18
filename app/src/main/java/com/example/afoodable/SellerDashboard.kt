package com.example.afoodable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView



class SellerDashboard : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_dashboard)
        bottomNavigationView = findViewById(R.id.seller_bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.seller_bottom_products -> {
                    replaceFragment(SellerProductsFragment())
                    true
                }
                R.id.seller_bottom_transactions -> {
                    replaceFragment(SellerTransactionsFragment())
                    true

                }
                R.id.seller_bottom_notifications -> {
                    replaceFragment(SellerNotificationsFragment())
                    true
                }

                R.id.seller_bottom_account-> {
                    replaceFragment(SellerAccountFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(SellerProductsFragment())
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }
}
