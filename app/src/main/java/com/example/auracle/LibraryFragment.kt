package com.example.auracle

import android.content.Intent
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
            startActivity(Intent(requireContext(), FavoritesActivity::class.java))
        }

        return binding.root
    }

}