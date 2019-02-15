package com.ecommerce.back.util;

import com.ecommerce.back.exception.IllegalException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class ImgUtil {
    private static final Set<String> SUPPORTED_TYPE = new HashSet<>();

    /**
     * COS secretId
     */
    private static final String secretId = "AKIDXqupZ3sVN1RfeQ7g3BOO9h6MGwNaJNwM";
    /**
     * COS secretKey
     */
    private static final String secretKey = "KTTH8pIWDk9JEzXv30iIdfS1LPPH8k7G";
    /**
     * COS bucketName
     */
    private static final String bucketName = "imgs-1257617444";
    /**
     * COS regionName
     */
    private static final String regionName = "ap-chengdu";
    private static final COSClient cosClient;

    static {
        //初始化支持的图片类型
        SUPPORTED_TYPE.add("png");
        SUPPORTED_TYPE.add("jpg");

        //初始化COS客户端
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        cosClient = new COSClient(cred, clientConfig);
    }

    /**
     * 将图片的Base64编码的字节数组和图片类型上传为COS端的图片，并返回图片访问地址
     * @param base64ImgBytes 图片的Base64编码的字节数组
     * @param imgType 图片类型
     * @return 图片访问地址
     * @throws IOException 上传IOException
     * @throws IllegalException 不支持的图片类型
     */
    public static String Base64BytesToLocalImg(byte[] base64ImgBytes, String imgType) throws IOException, IllegalException {
        if (!SUPPORTED_TYPE.contains(imgType))
            throw new IllegalException("图片类型",imgType,"不支持该图片类型");

        byte[] imgBytes = Base64.getDecoder().decode(base64ImgBytes);
        String fileName = UUID.randomUUID().toString().replace("-","").substring(0,10) + "." + imgType;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBytes);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imgBytes.length);
        PutObjectResult putObjectResult = cosClient.putObject(bucketName, fileName, inputStream, objectMetadata);
        inputStream.close();

        return "https://" + bucketName + ".cos" + "." + regionName + ".myqcloud.com/" + fileName;
    }

    /**
     * 将多张图片的Base64+UTF-8编码的字符串和图片类型上传为COS端的图片，并返回图片访问地址数组
     * @param imgBase64Strings 多张图片的Base64+UTF-8编码的字符串
     * @param imgTypes 多张图片的图片类型
     * @return 图片访问地址数组
     * @throws IOException 上传IOException
     * @throws IllegalException 不支持的图片类型
     */
    public static String[] MultiBase64StringsToLocalImg(String[] imgBase64Strings, String[] imgTypes) throws IOException, IllegalException {
        String[] imgUrls = new String[imgBase64Strings.length];
        for (int i = 0; i < imgBase64Strings.length; i++) {
            byte[] base64ImgBytes = imgBase64Strings[i].getBytes(StandardCharsets.UTF_8);
            String imgType = imgTypes[i];
            imgUrls[i] = ImgUtil.Base64BytesToLocalImg(base64ImgBytes, imgType);
        }

        return imgUrls;
    }

    /**
     * 检查imgBase64String, 不能为空或长度超过maxLength
     * @param imgBase64String 图片经Base64编码后的字符串
     * @param maxLength 最大长度
     * @throws IllegalException 提交信息不合法
     */
    public static void checkImgBase64String(String imgBase64String, int maxLength) throws IllegalException {
        if (imgBase64String == null || imgBase64String.length() == 0) throw new IllegalException("图片Base64字符串", imgBase64String,"不能为空");
        if (imgBase64String.length() >= maxLength) throw new IllegalException("图片Base64字符串", imgBase64String,"长度不能超过" + maxLength);
    }
}