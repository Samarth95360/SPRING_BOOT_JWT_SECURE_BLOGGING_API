package com.integration.imageStorage.dirCreation.fileCreation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileService {

    @Value("${user-dir.base-path}")
    private String baseDir;

    public String createDir(String userName,String profile){
        String fullPath = baseDir+ userName+"/"+profile;
        File file = new File(fullPath);
        if(!file.exists()){
            if(file.mkdirs()){
                return fullPath;
            }else{
                return null;
            }
        }
        return fullPath;

    }

}
