package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.auracle.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(layoutInflater)

        binding.FavouritesButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentDisplay, FavoriteFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return binding.root
    }

}