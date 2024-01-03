package com.example.afoodable

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TransactionsFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        // Return the total number of fragments
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ConfirmationFragment()
            1 -> PreparingOrderFragment()
            2 -> PickUpDeliveryFragment()
            3 -> CompletedFragment()
            4 -> FailedFragment()
            5 -> CancelledFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
