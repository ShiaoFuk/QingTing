package com.example.qingting.Bean;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Music {
    Integer id;
    String name;
    Integer duration;
    String path;
    String genre;  // 曲风
    String tempo;  // 速度
    Date createdTime;
    Date updatedTime;
}