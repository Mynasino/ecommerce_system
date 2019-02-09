package com.ecommerce.back.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class ImgUtil {
    private static final Set<String> SUPPORTED_TYPE = new HashSet<>();
    private static final String STORED_PATH = new File(
            ImgUtil.class.getResource("/").getPath()
    ).getParent() + "\\resources\\static\\img";

    static {
        SUPPORTED_TYPE.add("png");
        SUPPORTED_TYPE.add("jpg");
    }

    public static String Base64BytesToLocalImg(byte[] base64ImgBytes, String imgType) throws IOException, IllegalStateException {
        if (!SUPPORTED_TYPE.contains(imgType))
            throw new IllegalStateException("File type not supported: " + imgType);

        byte[] imgBytes = Base64.getDecoder().decode(base64ImgBytes);
        String fileName = UUID.randomUUID().toString().replace("-","").substring(0,10) + "." + imgType;
        File file = new File(STORED_PATH + "\\" + fileName);
        if (!file.createNewFile())
            throw new IllegalStateException("Create file " +  fileName +  "in server failed, already exists");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imgBytes);
        fos.flush();
        fos.close();

        return fileName;
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
