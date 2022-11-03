package com.bykea.task.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bykea.task.R
import com.bykea.task.data.remote.model.ResultsItem

class SongsAdapter(
    songs: List<ResultsItem?>?,
    itemListener: SelectTrackListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selectTrackListener: SelectTrackListener = itemListener
    private var songsList: List<ResultsItem?>? = songs
    private var playView: View? = null

    interface SelectTrackListener {
        fun onItemClick(currentItem: ResultsItem)
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var coverImg: ImageView = itemView.findViewById(R.id.itemSong_coverImg)
        var playingImg: ImageView = itemView.findViewById(R.id.itemSong_playingImg)
        var songName: TextView = itemView.findViewById(R.id.itemSong_songTitle)
        var artistName: TextView = itemView.findViewById(R.id.itemSong_artistName)
        var albumName: TextView = itemView.findViewById(R.id.itemSong_albumName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = songsList?.get(position)
        currentItem?.let {
            updateView(currentItem, viewHolder as ViewHolder)
        }
    }

    private fun updateView(currentItem: ResultsItem, viewHolder: ViewHolder) {
        viewHolder.coverImg.load(currentItem.artworkUrl100) {
            crossfade(true)
            placeholder(R.drawable.no_image)
        }

        viewHolder.songName.text = currentItem.trackName
        viewHolder.artistName.text = currentItem.artistName
        viewHolder.albumName.text = currentItem.collectionName
        viewHolder.itemView.setOnClickListener {
            if (playView != viewHolder.playingImg) {
                if (!currentItem.previewUrl.isNullOrEmpty()) {
                    playView?.let {
                        it.visibility = View.GONE
                    }
                    playView = viewHolder.playingImg
                    viewHolder.playingImg.visibility = View.VISIBLE
                    selectTrackListener.onItemClick(currentItem)
                }
            }
        }
    }

    fun clearList(songs: List<ResultsItem?>?) {
        this.songsList = songs
        songsList?.let { notifyItemRangeRemoved(0, it.size) }
    }

    override fun getItemCount(): Int {
        return songsList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}


