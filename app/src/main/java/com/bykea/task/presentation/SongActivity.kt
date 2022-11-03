package com.bykea.task.presentation

import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bykea.task.core.base.BaseActivity
import com.bykea.task.core.networking.Resource
import com.bykea.task.data.remote.model.ResultsItem
import com.bykea.task.databinding.ActivitySongsBinding
import com.bykea.task.utils.AppConstants
import com.bykea.task.utils.toast.ToastUtil
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.bykea.task.R
import com.bykea.task.core.connectivity.ConnectionLiveData
import com.bykea.task.utils.onDone
import com.bykea.task.utils.showError
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SongActivity : BaseActivity<ActivitySongsBinding>(), SongsAdapter.SelectTrackListener {

    @Inject
    lateinit var connectionLiveData: ConnectionLiveData
    private val viewModel by viewModels<SongViewModel>()
    private var songsAdapter: SongsAdapter? = null
    private var mediaPlayer: MediaPlayer? = null


    override fun getLayoutRes(): Int = R.layout.activity_songs

    override fun onInit() {
        initListeners()
        observeSongSearch()
        binding.activitySongsSongsRv.addItemDecoration(prepareDivider())
    }

    private fun initListeners() {
        binding.activitySongsSearchArtistEdt.onDone {
            if (binding.activitySongsSearchArtistEdt.text.toString().isNotEmpty())
                viewModel.searchSongs(binding.activitySongsSearchArtistEdt.text.toString())
        }

        binding.activitySongsPlayImg.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                togglePlayerSwitch(false)
            } else
                togglePlayerSwitch(true)
        }


        connectionLiveData = ConnectionLiveData(this@SongActivity)
        connectionLiveData.observe(this) { isNetworkAvailable ->
            if (!isNetworkAvailable)
                showError(
                    context = this@SongActivity,
                    errorMessage = getString(R.string.no_internet_error)
                )
        }
    }

    private fun populateSongs(songList: List<ResultsItem?>?) {
        if (songList.isNullOrEmpty()) {
            toggleEmptyScreen(true)
            if (songsAdapter != null) {
                songsAdapter?.clearList(ArrayList<ResultsItem>())
            }
        } else {
            toggleEmptyScreen(false)
            songsAdapter = SongsAdapter(songList, this)
            binding.activitySongsSongsRv.adapter = songsAdapter
            binding.activitySongsSongsRv.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun prepareDivider(): DividerItemDecoration {
        val divider = DividerItemDecoration(
            this,
            DividerItemDecoration.VERTICAL
        )

        ContextCompat.getDrawable(
            baseContext,
            R.drawable.separator
        )?.let {
            divider.setDrawable(
                it
            )
        }
        return divider
    }

    private fun observeSongSearch() {
        viewModel.searchResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showProgress()
                }
                is Resource.Success -> {
                    hideProgress()
                    populateSongs(it.data.results)
                }
                is Resource.ResourceError -> {
                    hideProgress()
                    when (it) {
                        is Resource.ResourceError.NetworkError -> {
                            showError(
                                context = this@SongActivity,
                                errorMessage = getString(R.string.internet_error_message)
                            )
                        }
                        is Resource.ResourceError.GenericError -> {
                            it.code?.let { code ->
                                when (code) {
                                    AppConstants.ResponseCode.STATUS_CLIENT_ERROR_402 -> {
                                        ToastUtil.show(R.string.generic_server_error)
                                    }
                                    AppConstants.ResponseCode.STATUS_CLIENT_ERROR_403 -> {
                                        ToastUtil.show(R.string.generic_server_error)
                                    }
                                    AppConstants.ResponseCode.STATUS_CLIENT_ERROR_404 -> {
                                        ToastUtil.show(R.string.generic_server_error)
                                    }
                                    AppConstants.ResponseCode.STATUS_CLIENT_ERROR_415 -> {
                                        ToastUtil.show(R.string.generic_server_error)
                                    }
                                    else -> {
                                        showError(
                                            context = this@SongActivity,
                                            errorMessage = getString(R.string.internet_error_message)
                                        )
                                    }
                                }
                            }
                        }
                        else -> {

                        }
                    }
                }
            }
        }

    }

    override fun screenName(): String = SongActivity::class.java.simpleName
    override fun onItemClick(currentItem: ResultsItem) {
        showProgress()
        mediaPlayer?.let {
            it.stop()
        }
        val uri = Uri.parse(currentItem.previewUrl)
        mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer?.apply {
            setOnPreparedListener { hideProgress() }
            setOnCompletionListener { stopMusic() }
            togglePlayer(true)
        }

    }

    private fun stopMusic() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun togglePlayer(shouldShow: Boolean) {
        if (shouldShow) {
            binding.activitySongsPlayerContainer.visibility = View.VISIBLE
            togglePlayerSwitch(true)
        } else {
            binding.activitySongsPlayerContainer.visibility = View.GONE
            togglePlayerSwitch(false)
        }
    }

    private fun togglePlayerSwitch(shouldPlay: Boolean) {
        if (shouldPlay) {
            mediaPlayer?.start()
            binding.activitySongsPlayImg.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_pause
                )
            )
        } else {
            mediaPlayer?.pause()
            binding.activitySongsPlayImg.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_play
                )
            )
        }
    }

    private fun toggleEmptyScreen(shouldShow: Boolean) {
        if (shouldShow) {
            binding.activitySongsNoTracksTxt.text =
                resources.getString(R.string.no_tracks_found_error)
            binding.activitySongsNoTracksTxt.visibility = View.VISIBLE
            binding.activitySongsSongsRv.visibility = View.GONE
        } else {
            binding.activitySongsNoTracksTxt.text =
                resources.getString(R.string.no_tracks_found_error)
            binding.activitySongsNoTracksTxt.visibility = View.GONE
            binding.activitySongsSongsRv.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }
}