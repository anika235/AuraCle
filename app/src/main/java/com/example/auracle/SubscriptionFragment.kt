package com.example.auracle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auracle.com.example.auracle.subscribe_adapter
import com.example.auracle.databinding.FragmentSubscriptionBinding
import com.example.auracle.datapack.listennote.ListenPodcastLong

class SubscriptionFragment : Fragment() {
    private lateinit var binding: FragmentSubscriptionBinding
    private lateinit var podcastId: String
    private lateinit var adapter: subscribe_adapter
    companion object{
        var subscribes: ArrayList<ListenPodcastLong> = ArrayList()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            podcastId = it.getString("podcastId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=  FragmentSubscriptionBinding.inflate(layoutInflater)

        binding.subscribeRV.setHasFixedSize(true)
        binding.subscribeRV.setItemViewCacheSize(13)
        binding.subscribeRV.layoutManager = LinearLayoutManager(requireContext())
        adapter = subscribe_adapter(subscribes, this::toPodcastDetails )
        binding.subscribeRV.adapter = adapter
        return binding.root
    }

    private fun toPodcastDetails(podcastList: ArrayList<ListenPodcastLong>, position: Int) {
        val podcastId = podcastList[position].id
        val fragment = PodcastDetailsFragment()
        val args = Bundle()
        args.putString("podcastId", podcastId)
        fragment.arguments = args

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentDisplay, fragment)
        fragmentTransaction.commit()
    }


}