package com.example.afoodable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2

class TransactionsFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: TransactionsFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transactions, container, false)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager2 = view.findViewById(R.id.viewPager2)

        adapter = TransactionsFragmentAdapter(childFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.file_6687180))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.approved_7818011))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.truck_6417794))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.stickynote_6042989))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.baseline_warning_24))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.page_5451441))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        return view
    }

}
