package ru.fratask.practice.map.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TokenStorage {

    private BiMap<String, Long> tokenUserIdBiMap = HashBiMap.create();

    private static TokenStorage instance;

    public static TokenStorage getInstance(){
        if (instance == null){
            instance = new TokenStorage();
        }
        return instance;
    }
}
