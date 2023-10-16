package com.example.auracle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.auracle.databinding.FragmentLibraryBinding
import com.example.auracle.firebase.Authenticate
import com.example.auracle.podcastcard.PodcastCard
import com.example.auracle.podcastcard.PodcastCardAdapter

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(layoutInflater)

        binding.btnSignOut.setOnClickListener{
            Authenticate().signOut()
            requireActivity().startActivity(Intent(requireActivity(), StartPage::class.java))
        }

        return binding.root
    }

}