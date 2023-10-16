package com.example.auracle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.auracle.api.ApiTester
import com.example.auracle.databinding.FragmentHomeBinding
import com.example.auracle.firebase.Authenticate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate((layoutInflater))

        binding.btnHomeSignOut.setOnClickListener {
            Authenticate().signOut()
            requireActivity().startActivity(Intent(requireActivity(), StartPage::class.java))
        }

        lifecycleScope.launch(Dispatchers.IO) {
            ApiTester().testApi()
        }

        return binding.root
    }



}