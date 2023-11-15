package com.example.auracle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.auracle.databinding.FragmentLibraryBinding
import com.example.auracle.com.example.auracle.api.firebase.Authenticate
import com.example.auracle.com.example.auracle.api.firebase.FirebaseRealtime
import com.example.auracle.com.example.auracle.datapack.User
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(layoutInflater)

        getUserDetails()
        registerInteractive()

        return binding.root
    }

    private fun registerInteractive() {
        binding.SubscriptionButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentDisplay, SubscriptionFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.FavouritesButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentDisplay, FavoriteFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        binding.btnSignOut.setOnClickListener {
            Authenticate.getInstance().signOut()
            val intent = Intent(requireContext(), StartPage::class.java)
            startActivity(intent)
        }
    }

    private fun getUserDetails() {

        val currentUser = FirebaseRealtime.getInstance().getCurrentUserDetails()
        if (currentUser == null) {
            val str = "Sign In to see your profile"
            binding.userMail.text = str
            binding.userName.text = ""
            return
        }

        binding.profileSkeleton.showSkeleton()
        currentUser.addOnCompleteListener {
            Log.i("firebase", "Got value ${it.result?.value}")
            if (it.isSuccessful) {
                val user = it.result
                binding.userMail.text = user?.child("email")?.value.toString()
                binding.userName.text = user?.child("name")?.value.toString()
            }
            else
                Snackbar.make(binding.btnSignOut, "Login Unsuccessful: ${it.exception?.message}", Snackbar.LENGTH_LONG).show()

            binding.profileSkeleton.showOriginal()
        }

    }

}