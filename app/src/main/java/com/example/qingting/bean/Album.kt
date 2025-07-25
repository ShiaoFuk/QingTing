package com.example.qingting.bean

/**
 * songList允许为空，预加载的时候在主页直接加载好不为空，否则为null，进入歌单页面才加载
 */
data class Album(val songList: List<Music>?, val playListId: Int): PlayList()