package com.example.auracle

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.InputBinding
import androidx.fragment.app.Fragment
import com.example.auracle.databinding.ActivityHomepageBinding
import com.example.auracle.firebase.Authenticate

class Homepage : AppCompatActivity() {

    private val TAG = "HomePage"
    private lateinit var binding : ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replacefragment(HomeFragment())

        binding.bottomNavigationView.setOnItemReselectedListener {

            when(it.itemId){
                R.id.home_fragment -> replacefragment(HomeFragment())
                R.id.explore_fragment -> replacefragment(exploreFragment())
                R.id.library_fragment -> replacefragment(LibraryFragment())

                else->{
                }
            }
            true
        }
        Log.d(TAG, Authenticate().user().toString())
    }

    private fun replacefragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

}