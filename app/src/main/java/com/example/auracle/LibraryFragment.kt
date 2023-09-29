package com.example.auracle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.auracle.firebase.Authenticate
import com.example.auracle.podcastcard.PodcastCard
import com.example.auracle.podcastcard.PodcastCardAdapter

class LibraryFragment : Fragment() {

    private lateinit var signoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_library, container, false)

        signoutButton = v.findViewById(R.id.signoutbutton)
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