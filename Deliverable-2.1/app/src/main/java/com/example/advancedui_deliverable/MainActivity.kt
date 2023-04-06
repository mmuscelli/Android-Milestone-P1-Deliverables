package com.example.advancedui_deliverable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TabLayout
        val mTabLayout: TabLayout = findViewById(R.id.mTabLayout)
        val mViewPager: ViewPager2 = findViewById(R.id.mViewPager)

        // Since we have 3 pre-defined tab item, tab count would be 3.
        mViewPager.adapter = MainFragmentAdapter(this, 3)

        // List of string that holds name of the tab.
        val tabName = arrayOf<String>("FIRST", "SECOND", "THIRD")
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = tabName[position]
            Toast.makeText(
                this,
                "Tab ${tab.text} at position $position is selected",
                Toast.LENGTH_LONG
            ).show()
        }.attach()
    }

}

class MainFragmentAdapter(fragmentActivity: FragmentActivity, var tabCount: Int)
    : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return tabCount // Default placeholder value
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // ImageView with first image
                return ImageFragment(R.drawable.donut)
            }
            1 -> {
                // ImageView with second image
                return ImageFragment(R.drawable.coffee)
            }
            2 -> {
                // ListView
                return ListFragment()
            }
        }
        // It is required to explicitly state a default return option.
        return ImageFragment(com.google.android.material.R.drawable.mtrl_ic_error)
    }
}