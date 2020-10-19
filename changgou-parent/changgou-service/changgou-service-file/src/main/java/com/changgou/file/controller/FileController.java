package com.changgou.file.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSClient;
import entity.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传
 */
@RestController
@CrossOrigin

public class FileController {

    @PostMapping("/upload")
    public  String upload(@RequestParam("file")MultipartFile file){

        try {
            FastDFSFile fastDFSFile = new FastDFSFile(file.getName(),
                    file.getBytes(),
                    StringUtils.getFilenameExtension(file.getOriginalFilename()));
           String[] uploads =  FastDFSClient.upload(fastDFSFile);
          return FastDFSClient.getTrackerUrl() + "/" +uploads[0]+"/"+ uploads[1];
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }
}
