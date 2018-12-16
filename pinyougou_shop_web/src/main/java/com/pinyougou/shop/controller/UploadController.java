package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    /**
     * 基于FastDFS实现商品录入之图片上传功能
     * MultipartFile springmvc接收上传文件的API
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){

        try {
            //获取文件源名称
            String originalFilename = file.getOriginalFilename();
            //获取扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //基于FastDFS工具类实现图片上传功能
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //例如：group1/M00/00/00/wKgZhVwQbImAEoT1AAA0JGtMezo546.jpg
            String filePath = fastDFSClient.uploadFile(file.getBytes(), extName);
            //System.out.println(filePath);
            //图片地址
            String fileUrl=FILE_SERVER_URL+filePath;
            return new Result(true,fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
