package com.example.auracle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auracle.com.example.auracle.SubscribeAdapter
import com.example.auracle.com.example.auracle.api.firebase.FirebaseRealtime
import com.example.auracle.databinding.FragmentSubscriptionBinding
import com.example.auracle.datapack.listennote.ListenPodcastLong
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import com.faltenreich.skeletonlayout.applySkeleton

class SubscriptionFragment : Fragment() {
    private lateinit var binding: FragmentSubscriptionBinding
    private val firebaseRealtime = FirebaseRealtime.getInstance()

    companion object{
        var subscribes: ArrayList<ListenPodcastLong> = ArrayList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=  FragmentSubscriptionBinding.inflate(layoutInflater)

        binding.rcvSubscribe.setHasFixedSize(true)
        binding.rcvSubscribe.setItemViewCacheSize(13)
        binding.rcvSubscribe.layoutManager = LinearLayoutManager(requireContext())

        val rcvSubscribeSkeleton = binding.rcvSubscribe.applySkeleton(R.layout.favoriteview)
        rcvSubscribeSkeleton.showSkeleton()

        firebaseRealtime.getSubscriptions()?.addOnCompleteListener{
            if(it.isSuccessful){
                val values = it.result!!.value as HashMap<*, *>

                val podcastList: ArrayList<ListenSearchPodcast> = ArrayList()

                values.forEach { (_, va) ->

                    Log.w("SubscriptionFragment", "va: $va")

                    val episode = ListenSearchPodcast(va as Map<*, *>)
                    podcastList.add(episode)
                }
                rcvSubscribeSkeleton.showOriginal()
                binding.rcvSubscribe.adapter = SubscribeAdapter(podcastList, this::toPodcastDetails)
            }
        }
        return binding.root
    }

    private fun toPodcastDetails(podcast: ListenSearchPodcast) {


        val podcastId = podcast.id
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