package com.example.auracle

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.auracle.com.example.auracle.api.firebase.FirebaseRealtime
import com.example.auracle.com.example.auracle.api.roomapi.AppDatabase
import com.example.auracle.com.example.auracle.datapack.room.RoomEpisode
import com.example.auracle.com.example.auracle.viewmodel.HomeViewModel
import com.example.auracle.databinding.FragmentPodcastDetailsBinding
import com.example.auracle.datapack.listennote.ListenEpisodeShort
import com.example.auracle.datapack.listennote.ListenSearchPodcast
import com.example.auracle.episodecard.EpisodeCardAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL


class PodcastDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPodcastDetailsBinding
    private lateinit var db: AppDatabase
    private lateinit var podcastId: String
    private val podcastViewModel: HomeViewModel by activityViewModels()
    private val firebaseRealtime = FirebaseRealtime.getInstance()
    private var isSubscribe: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            podcastId = it.getString("podcastId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPodcastDetailsBinding.inflate(layoutInflater)
        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "AuraCle").build()

        binding.rcvPodcastEpisodeList.addItemDecoration(
            MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcvPodcastEpisodeList.layoutManager = LinearLayoutManager(requireContext())

        podcastViewModel.playlistAvailable.observe(viewLifecycleOwner) {
            initializeLayout()
        }
        podcastViewModel.offlineAvailable.observe(viewLifecycleOwner) {
            initializeLayout()
        }

        checkIfSubscribed()
        getAvailableOfflineList()
        getPodcastDetails()

        return binding.root
    }

    private fun registerInteractive() {

        binding.btnToWeb.setOnClickListener {
            val uri = Uri.parse(podcastViewModel.playlist.website)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        binding.btnSubscribe.setOnClickListener {
            if (isSubscribe) {
                val isLoggedIn = firebaseRealtime.removeFromSubscriptions(podcastId)
                if (isLoggedIn == null){
                    Snackbar.make(binding.root, "Log In to Perform Action", Snackbar.LENGTH_LONG).show()
                    isSubscribe = !isSubscribe
                }
                else
                    binding.btnSubscribe.setIconResource(R.drawable.baseline_playlist_add_24)
            }
            else {
                val isLoggedIn = firebaseRealtime.addToSubscription(ListenSearchPodcast(podcastViewModel.playlist))
                if (isLoggedIn == null){
                    Snackbar.make(binding.root, "Log In to Perform Action", Snackbar.LENGTH_LONG).show()
                    isSubscribe = !isSubscribe
                }
                else
                    binding.btnSubscribe.setIconResource(R.drawable.baseline_playlist_add_check_24)
            }
        }
    }

    private fun checkIfSubscribed() {

        firebaseRealtime.checkSubscription(podcastId)?.addOnSuccessListener {

            if (it.exists()) {
                isSubscribe = true
                binding.btnSubscribe.setIconResource(R.drawable.baseline_playlist_add_check_24)
            }
            else {
                isSubscribe = false
                binding.btnSubscribe.setIconResource(R.drawable.baseline_playlist_add_24)
            }
        }
    }

    private fun getAvailableOfflineList() {
        podcastViewModel.retrieveOfflinePodcast(podcastId, db.episodeDao())
    }

    private fun foundOffline() {
        for (episode in podcastViewModel.playlist.episodes) {
            for (offlineEpisode in podcastViewModel.offlineAvailableList) {
                if (episode.id == offlineEpisode.id) {
                    episode.offlineLocation = offlineEpisode.audioLocation
                }
            }
        }
    }

    private fun initializeLayout() {

        if (podcastViewModel.playlistAvailable.value == true && podcastViewModel.offlineAvailable.value == true) {

            foundOffline()
            registerInteractive()

            val podcastDetails = podcastViewModel.playlist

            val time = ""
            if (podcastDetails.audioLengthSec!! / 3600 > 0) {
                time.plus("${podcastDetails.audioLengthSec!! / 3600}h ")
            }
            if (podcastDetails.audioLengthSec!! / 60 > 0) {
                time.plus("${podcastDetails.audioLengthSec!! / 60}m ")
            }

            Picasso.get().load(podcastDetails.thumbnail).into(binding.imgPodcastThumbnail)
            binding.txtPodcastTitle.text = podcastDetails.title
            binding.txtPodcastAuthor.text = podcastDetails.publisher
            binding.txtPodcastTotalTime.text = time
            binding.txtPodcastDescription.text = podcastDetails.description

            binding.rcvPodcastEpisodeList.adapter =
                EpisodeCardAdapter(podcastDetails.episodes, this::toPlayer, this::onDownload)

            binding.podcastDetailSkeleton.showOriginal()

        }
    }

    private fun getPodcastDetails() {
        binding.podcastDetailSkeleton.showSkeleton()
        podcastViewModel.retrievePodcast(podcastId)
    }

    private fun toPlayer(podcastEpisodes: ArrayList<ListenEpisodeShort>, position: Int) {

        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("episodeList", podcastEpisodes)
        bundle.putInt("index", position)
        bundle.putString("class", "PodcastDetailsFragment")
        playerFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentDisplay, playerFragment, PlayerFragment.tag)
            .addToBackStack(null)
            .commit()

    }

    private fun onDownload(position: Int) {

        val episode = podcastViewModel.playlist.episodes[position]

        if (episode.offlineLocation.isNullOrBlank()) {

            Log.w("TTTTTT", "Downloading ${episode.title}")

            val url = URL(episode.audio)
            lifecycleScope.launch(Dispatchers.IO) {

                try {
                    val mp3Data = url.readBytes()

                    val writeFile = File(requireContext().filesDir, "${episode.id}.mp3")
                    writeFile.createNewFile()
                    writeFile.writeBytes(mp3Data)

                    val saveEpisode = RoomEpisode(episode.id!!, podcastId, episode.title, episode.thumbnail, episode.audioLengthSec, writeFile.absolutePath)
                    val episodeDao = db.episodeDao()
                    episodeDao.insert(saveEpisode)

                    episode.offlineLocation = writeFile.absolutePath

//                    Log.w("TTTTTT", "Downloading Done.")

                } catch (e: Exception) {
//                    Log.w("TTTTTT", "Downloading Failed: $e")
                } finally {
                    withContext(Dispatchers.Main) {
                        episode.isDownloading = false
                        binding.rcvPodcastEpisodeList.adapter?.notifyItemChanged(position)
                    }
                }

            }

        } else {

            // This beautiful piece of kotlin code deletes the file ar location if it exists
            episode.offlineLocation?.let { File(it) }?.delete()
            lifecycleScope.launch(Dispatchers.IO) {


                try {
                    val episodeDao = db.episodeDao()
                    episodeDao.delete(episode.id!!)
                } catch (e: Exception) {
                    Log.w("TTTTTT", "Downloading Failed: $e")

                } finally {
                    withContext(Dispatchers.Main) {
                        episode.offlineLocation = null
                        episode.isDownloading = false
                        binding.rcvPodcastEpisodeList.adapter?.notifyItemChanged(position)
                    }
                }

            }


        }
    }

    fun subscribechecker(id: String?): Int {
        isSubscribe = false
        SubscriptionFragment.subscribes.forEachIndexed { index, listenEpisodeLong ->
            if (id == listenEpisodeLong.id) {
                isSubscribe = true
                return index
            }
        }
        return -1
    }
}