package com.example.qingting.Bean;

import lombok.Data;

@Data
public class PlayList {
    Integer id;
    Integer userId;
    String name;
    Integer likes = 0;
    Integer playTimes = 0;
    String url;  // 图片url
}
