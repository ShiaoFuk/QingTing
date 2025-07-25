package com.example.qingting.bean;

public class Song {
    String id;
    String name;
    String singer;
    // 优先获取path
    // 如果不存在path则开始缓存，使用id发起请求，缓存到cache，将cache的数据解析到本地，然后设置path
    String path;  // 音乐在本地保存的地址，用于播放

    public Song() {
    }

    public Song(String id, String name, String singer, String path) {
        this.id = id;
        this.name = name;
        this.singer = singer;
        this.path = path;
    }
}
