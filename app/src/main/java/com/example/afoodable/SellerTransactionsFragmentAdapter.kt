package com.example.afoodable

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SellerTransactionsFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        // Return the total number of fragments
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SellerOrdersFragment()
            1 -> SellerPreparingFragment()
            2 -> SellerPickUpDeliveryFragment()
            3 -> SellerCompletedFragment()
            4 -> SellerFailedFragment()
            5 -> SellerCancelledFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
