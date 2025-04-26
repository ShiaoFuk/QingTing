package com.example.qingting.Adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qingting.Bean.Album


class AlbumAdapter(var albumListList: List<List<Album>>): RecyclerView.Adapter<AlbumViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        // 0 1 2
        val view: View = when (viewType) {
            1 -> View.inflate(parent.context, com.example.qingting.R.layout.album_list_layout_style2, null)
            2 -> View.inflate(parent.context, com.example.qingting.R.layout.album_list_layout_style3, null)
            else -> View.inflate(parent.context, com.example.qingting.R.layout.album_list_layout_style1, null)  // 默认样式
        }
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = albumListList.size

    fun updateData(newAlbumListList: List<List<Album>>) {
        albumListList = newAlbumListList
        // 不能简单使用这个计算
        notifyDataSetChanged()
    }
}

class AlbumViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}