package com.example.qingting.Bean;

import lombok.Data;

@Data
public class PlayList {
    Integer id;
    Integer userId;
    String name;
    Integer likes;
    Integer playTimes;
    String url;  // 图片url
}
