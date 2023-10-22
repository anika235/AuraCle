package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.auracle.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate((layoutInflater))

//        binding.btnHomeToExplore.setOnClickListener {
//            val txt = binding.thing.text.toString()
//            lifecycleScope.launch(Dispatchers.IO) {
//
//                ListenNoteApi().search(txt)
//            }
//        }

        return binding.root
    }


}