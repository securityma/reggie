package com.kobe.reggie.controller;

import com.kobe.reggie.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private  String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        String origin=file.getOriginalFilename();
        String suffix=origin.substring(origin.lastIndexOf("."));
        String fileName= UUID.randomUUID().toString()+suffix;
        File dir=new File(basePath);
        if(!dir.exists()){
            dir.mkdir();
        }

        try{
            file.transferTo(new File(basePath+fileName));
        }catch (IOException e){
            e.printStackTrace();
        }
        return  R.success(fileName);
    }
    @GetMapping("/download")
    public  void download(String name, HttpServletResponse response)
    {
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
            ServletOutputStream outputStream= response.getOutputStream();
            response.setContentType("image/jpeg");
            int len=0;
            byte [] bytes=new byte[1024];
            while((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
