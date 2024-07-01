package com.dicoding.tanaminai.view.prediction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity, private val predictionResult: String) :
    FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ResultFragment()
        fragment.arguments = Bundle().apply {
            putInt(ResultFragment.ARG_POSITION, position + 1)
            putString(ResultFragment.ARG_PREDICTIONRESULT, predictionResult)
        }
        return fragment
    }

}