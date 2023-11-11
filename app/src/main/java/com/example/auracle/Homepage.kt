package com.example.auracle

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.auracle.com.example.auracle.viewmodel.HomeViewModel
import com.example.auracle.com.example.auracle.viewmodel.PlaylistViewModel
import com.example.auracle.databinding.ActivityHomepageBinding
import com.example.auracle.firebase.Authenticate

class Homepage : AppCompatActivity() {

    private val viewModel: PlaylistViewModel by viewModels()
    private lateinit var binding : ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home_fragment -> replaceFragment(HomeFragment())
                R.id.explore_fragment -> replaceFragment(ExploreFragment())
                R.id.library_fragment -> replaceFragment(LibraryFragment())

                else->{
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentDisplay.id, fragment)
        fragmentTransaction.commit()
    }

}