package com.example.auracle

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.auracle.com.example.auracle.api.roomapi.AppDatabase
import com.example.auracle.com.example.auracle.datapack.room.RoomEpisode
import com.example.auracle.com.example.auracle.rcvdownloaded.DownloadedAdapter
import com.example.auracle.com.example.auracle.viewmodel.HomeViewModel
import com.example.auracle.databinding.FragmentDownloadBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var db: AppDatabase
//    private lateinit var rcvDownloadedSkeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDownloadBinding.inflate(inflater, container, false)
        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "AuraCle").build()

        binding.rcvDownloaded.layoutManager = GridLayoutManager(requireContext(), 2)

        val rcvDownloadedSkeleton = binding.rcvDownloaded.applySkeleton(R.layout.download_card)
        rcvDownloadedSkeleton.showSkeleton()

        homeViewModel.offlineAvailable.observe(viewLifecycleOwner) {
            Log.w("TTTTTT", "Offline Available: ${homeViewModel.offlineAvailableList}")
            Log.w("TTTTTT", "Offline Available: $it")
            if (it) {
                rcvDownloadedSkeleton.showOriginal()
                binding.rcvDownloaded.adapter = DownloadedAdapter(homeViewModel.offlineAvailableList, this::toPlayer, this::onDownload)
            }
        }

        homeViewModel.retrieveOffline(db.episodeDao())

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun toPlayer(episode: RoomEpisode) {

        val listenEpisode = ListenEpisodeShort(episode.id,null,episode.audioLocation,null,episode.title,episode.thumbnail,null,null,null,null,episode.audioLength,null,null,null,episode.audioLocation,false)

        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("episodeList", arrayListOf(listenEpisode))
        bundle.putInt("index", 0)
        bundle.putString("class", "DownloadFragment")
        playerFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentDisplay, playerFragment, PlayerFragment.tag)
            .addToBackStack(null)
            .commit()

    }

    private fun onDownload(position: Int) {

        val episode = homeViewModel.offlineAvailableList[position]

        episode.audioLocation?.let { File(it) }?.delete()
        lifecycleScope.launch(Dispatchers.IO) {

            try {
                val episodeDao = db.episodeDao()
                episodeDao.delete(episode.id)
            } catch (e: Exception) {
                Log.w("TTTTTT", "Un-Downloading Failed: $e")

            } finally {
                withContext(Dispatchers.Main) {
                    homeViewModel.offlineAvailableList.removeAt(position)
                    binding.rcvDownloaded.adapter?.notifyItemRemoved(position)
                }
            }

        }
    }

}