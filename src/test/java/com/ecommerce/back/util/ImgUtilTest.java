package com.ecommerce.back.util;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ImgUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(ImgUtilTest.class);

    @Test
    public void Base64BytesToLocalImgTest() throws NoSuchFieldException, IllegalAccessException, IOException {
        Field field = ImgUtil.class.getDeclaredField("STORED_PATH");
        field.setAccessible(true);
        String STORED_PATH = (String)field.get(new ImgUtil());

        byte[] imgBytes = getImgBytes(STORED_PATH + "\\" + "test.png");
        //logger.info(new String(imgBytes));
        byte[] base64ImgBytes = Base64.getEncoder().encode(imgBytes);
        //logger.info(new String(base64ImgBytes, StandardCharsets.UTF_8));
        String newFileName = ImgUtil.Base64BytesToLocalImg(base64ImgBytes, "png");

        byte[] newImgBytes = getImgBytes(STORED_PATH + "\\" + newFileName);

        Assert.assertEquals(imgBytes.length, imgBytes.length);
        for (int i = 0; i < imgBytes.length; i++)
            Assert.assertEquals(imgBytes[i],newImgBytes[i]);
    }

    @Test
    public void jsDataURLWithoutTypeToImgTest() throws IOException {
        String base64ImgStr = "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";
        ImgUtil.Base64BytesToLocalImg(base64ImgStr.getBytes(StandardCharsets.UTF_8), "png");
    }

    private byte[] getImgBytes(String absolutePath) throws IOException {
        FileInputStream fis = new FileInputStream(new File(absolutePath));
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, bytes.length);
        fis.close();

        return bytes;
    }
}
