package com.example.auracle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.auracle.firebase.Authenticate

class HomeFragment : Fragment() {

    private lateinit var signoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        signoutButton = v.findViewById(R.id.signOutButton)
        signoutButton.setOnClickListener{
            Authenticate().signOut()
            requireActivity().startActivity(Intent(requireActivity(), StartPage::class.java))
        }
//        val nxtbutton : Button = view.findViewById(R.id.explorebutton)
//        nxtbutton.setOnClickListener {
//            val fragment = exploreFragment()
//
//        }

        return v
    }

}