package com.ecommerce.back.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class ImgUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImgUtil.class);

    private static final Set<String> SUPPORTED_TYPE = new HashSet<>();

    private static final String secretId = "AKIDXqupZ3sVN1RfeQ7g3BOO9h6MGwNaJNwM";
    private static final String secretKey = "KTTH8pIWDk9JEzXv30iIdfS1LPPH8k7G";
    private static final String bucketName = "imgs-1257617444";
    private static final String regionName = "ap-chengdu";
    private static final COSClient cosClient;

    static {
        SUPPORTED_TYPE.add("png");
        SUPPORTED_TYPE.add("jpg");

        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        cosClient = new COSClient(cred, clientConfig);
    }

    public static String Base64BytesToLocalImg(byte[] base64ImgBytes, String imgType) throws IOException, IllegalStateException {
        if (!SUPPORTED_TYPE.contains(imgType))
            throw new IllegalStateException("File type not supported: " + imgType);

        byte[] imgBytes = Base64.getDecoder().decode(base64ImgBytes);
        String fileName = UUID.randomUUID().toString().replace("-","").substring(0,10) + "." + imgType;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBytes);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imgBytes.length);
        PutObjectResult putObjectResult = cosClient.putObject(bucketName, fileName, inputStream, objectMetadata);
        inputStream.close();

        return "https://" + bucketName + ".cos" + "." + regionName + ".myqcloud.com/" + fileName;
    }

    public static String[] MultiBase64BytesToLocalImg(String[] imgBase64Strings, String[] imgTypes) throws IOException, IllegalStateException {
        String[] imgUrls = new String[imgBase64Strings.length];
        for (int i = 0; i < imgBase64Strings.length; i++) {
            byte[] base64ImgBytes = imgBase64Strings[i].getBytes(StandardCharsets.UTF_8);
            String imgType = imgTypes[i];
            imgUrls[i] = ImgUtil.Base64BytesToLocalImg(base64ImgBytes, imgType);
        }

        return imgUrls;
    }
}