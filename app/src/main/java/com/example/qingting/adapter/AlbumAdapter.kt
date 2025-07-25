package com.example.qingting.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.qingting.bean.Album
import com.example.qingting.customview.MyDividerDecoration
import com.example.qingting.R
import com.example.qingting.utils.PostHelper
import com.example.qingting.databinding.AlbumLayoutBinding
import com.example.qingting.databinding.AlbumListLayoutBinding
const val dividerLength = 20

class AlbumAdapter(var albumListList: List<List<Album>>): RecyclerView.Adapter<AlbumViewHolder>() {
    companion object {
        const val TAG = "AlbumAdapter"
    }
    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        // 0 1 2
        val context = parent.context
        val binding: AlbumListLayoutBinding = AlbumListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val height = when (viewType) {
            1 -> context.resources.getDimension(R.dimen.album_list_height_style2)
            2 -> context.resources.getDimension(R.dimen.album_list_height_style3)
            else -> context.resources.getDimension(R.dimen.album_list_height_style1)
        }
        binding.albumListLayout.layoutParams.height =  height.toInt()
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val albumList = albumListList[position]
        val adapter = holder.albumListView.adapter as AlbumRowAdapter
        adapter.updateData(albumList)
    }

    override fun getItemCount(): Int = albumListList.size

    fun updateData(newAlbumListList: List<List<Album>>) {
        albumListList = newAlbumListList
        // 使用修改的内容
        Log.d(TAG, "更新数据 ${newAlbumListList.size} 条")
        PostHelper.runOnMainThread {
            notifyDataSetChanged()
        }
    }
}

class AlbumViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    val albumListView: RecyclerView = binding.root.findViewById(R.id.album_list_layout)
    init {
        albumListView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = AlbumRowAdapter()
            addItemDecoration(MyDividerDecoration(RecyclerView.HORIZONTAL, dividerLength, Color.argb(0, 0, 0, 0)))
        }
    }
}

class AlbumRowAdapter(var list: List<Album> = mutableListOf()): RecyclerView.Adapter<AlbumRowViewHolder>() {

    companion object {
        const val TAG = "AlbumRowAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumRowViewHolder {
        val b = AlbumLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumRowViewHolder(b)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AlbumRowViewHolder, position: Int) {
        val album: Album = list[position]
        val coverImage = holder.albumCover
        val context = holder.albumCover.context
        // 预加载gif
        Glide.with(context)
            .load(Uri.parse("file:///android_asset/loading.gif"))
            .addListener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return true  // 不处理
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // 强制加载动画引入后再开始加载真正的资源
                    if (album.url != null) {
                        Glide.with(context)
                            .load(album.url)
                            .error(R.drawable.error_icon)
                            .into(coverImage)
                    } else {
                        PostHelper.runOnMainThread {
                            coverImage.setImageResource(R.drawable.login_bg)
                        }
                    }
                    return false
                }
            })
            .into(coverImage)
    }


    fun updateData(newList: List<Album>) {
        list = newList
        // 使用修改的内容
        Log.d(TAG, "更新数据 ${newList.size} 条")
        PostHelper.runOnMainThread {
            notifyDataSetChanged()
        }
    }

}

class AlbumRowViewHolder(binding: AlbumLayoutBinding): RecyclerView.ViewHolder(binding.root) {
    val albumCover: ImageView = binding.root.findViewById(R.id.album_cover)
    init {
        val paddingLength = dividerLength / 2
        albumCover.setPadding(
            paddingLength,
            0,
            paddingLength,
            0
        )
    }
}