package com.example.auracle

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.auracle.com.example.auracle.viewmodel.HomeViewModel
import com.example.auracle.com.example.auracle.viewmodel.PlaylistViewModel
import com.example.auracle.databinding.ActivityHomepageBinding
import com.example.auracle.com.example.auracle.api.firebase.Authenticate

class Homepage : AppCompatActivity() {

    private val viewModel: PlaylistViewModel by viewModels()
    private lateinit var binding : ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(binding.fragmentDisplay.id, HomeFragment())

        viewModel.nowPlaying.observe(this) {
            nowPlayingManager(it)
        }

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home_fragment -> replaceFragment(binding.fragmentDisplay.id, HomeFragment())
                R.id.explore_fragment -> replaceFragment(binding.fragmentDisplay.id, ExploreFragment())
                R.id.library_fragment -> replaceFragment(binding.fragmentDisplay.id, LibraryFragment())

                else->{
                }
            }
            true
        }
    }

    private fun replaceFragment(view: Int, fragment: Fragment)
    {
        binding.fragmentNowPlaying.visibility = View.VISIBLE
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(view, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun nowPlayingManager(show: Boolean) {
        if (show) {
            replaceFragment(binding.fragmentNowPlaying.id, NowPlayingFragment())
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {

            binding.fragmentNowPlaying.visibility = View.GONE
            binding.bottomNavigationView.visibility = View.GONE
        }
    }

}