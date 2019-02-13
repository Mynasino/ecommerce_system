package com.ecommerce.back.dao;

import com.ecommerce.back.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getDao {
    private static final Logger logger = LoggerFactory.getLogger(getDao.class);

    private static final String TEMPLATE_ABSOLUTE_PATH = "E:\\back\\src\\test\\java\\com\\ecommerce\\back\\dao\\modelDAODemo.txt";
    private static final String MODEL_ABSOLUTE_PATH = "E:\\back\\src\\main\\java\\com\\ecommerce\\back\\model";
    private static final String MODEL_PACKAGE_NAME = "com.ecommerce.back.model";
    private static final String DAO_ABSOLUTE_PATH = "E:\\back\\src\\main\\java\\com\\ecommerce\\back\\dao";
    private static final String DAO_PACKAGE_NAME = "com.ecommerce.back.dao";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        File directory = new File(MODEL_ABSOLUTE_PATH);
        if (!directory.isDirectory()) throw new IllegalArgumentException("PATH not directory");
        File[] files = directory.listFiles();
        if (files == null)
            throw new IllegalArgumentException("No file in this PATH");
        else {
            //read the template string
            StringWriter sw = new StringWriter();
            FileReader fr = new FileReader(TEMPLATE_ABSOLUTE_PATH);
            char[] buffer = new char[50];
            int readCount;
            while ((readCount = fr.read(buffer)) != -1)
                sw.write(buffer, 0, readCount);
            String templateStr = sw.toString();
            fr.close(); sw.close();

            //write DAO
            for (File file : files) {
                if (file.getName().endsWith(".java")) {
                    String className = file.getName().replace(".java","");
                    logger.info("className: " + className);
                    //logger.info(Class.forName(PACKAGE_NAME + "." + className).getSimpleName());

                    File newFile = new File(DAO_ABSOLUTE_PATH + "//" + className + "DAO.java");
                    if (!newFile.createNewFile())
                        throw new IllegalStateException("File " + newFile.getAbsolutePath() + "already exists");
                    logger.info("start writing " + newFile.getAbsolutePath());
                    FileWriter fw = new FileWriter(newFile);
                    fw.write(
                            transDAO(templateStr, Class.forName(MODEL_PACKAGE_NAME + "." + className), DAO_PACKAGE_NAME)
                    );
                    fw.close();
                }
            }
        }
    }

    private static String transDAO(String origin, Class<?> clazz, String packageName) {
        Map<String,String> replaceMap = new HashMap<>();

        List<String> fieldNames = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields())
            fieldNames.add(field.getName());

        replaceMap.put("${package}",packageName);
        replaceMap.put("${DAOName}",clazz.getSimpleName() + "DAO");
        replaceMap.put("${tableName}",camelCaseToHungarian(clazz.getSimpleName()));
        replaceMap.put("${modelName}",clazz.getSimpleName());
        replaceMap.put("${modelFullName}",clazz.getName());
        replaceMap.put("${instanceName}",clazz.getSimpleName().replace(clazz.getSimpleName().charAt(0), Character.toLowerCase(clazz.getSimpleName().charAt(0))));

        StringBuilder selectFields = new StringBuilder();
        StringBuilder insertFields = new StringBuilder();
        StringBuilder insertFieldsDB = new StringBuilder();
        for (String fieldName : fieldNames) {
            selectFields.append(camelCaseToHungarian(fieldName) + ",");
            if (!fieldName.equals("id")) {
                insertFieldsDB.append(camelCaseToHungarian(fieldName) + ",");
                insertFields.append("#{" + fieldName + "},");
            }
        }
        selectFields.deleteCharAt(selectFields.length() - 1);
        insertFieldsDB.deleteCharAt(insertFieldsDB.length() - 1);
        insertFields.deleteCharAt(insertFields.length() - 1);

        replaceMap.put("${selectFields}",selectFields.toString());
        replaceMap.put("${insertFieldsDB}",insertFieldsDB.toString());
        replaceMap.put("${insertFields}",insertFields.toString());

        return stringReplaces(origin, replaceMap);
    }

    private static String stringReplaces(String origin, Map<String,String> replaceMap) {
        for (String regex : replaceMap.keySet())
            origin = origin.replace(regex, replaceMap.get(regex));
        return origin;
    }

    private static String camelCaseToHungarian(String origin) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < origin.length(); i++)
            if (Character.isUpperCase(origin.charAt(i))) {
                if (i != 0)
                    sb.append("_");
                sb.append(Character.toLowerCase(origin.charAt(i)));
            } else
                sb.append(origin.charAt(i));

        return sb.toString();
    }

    @Test
    public void stringReplaceTest() {
        Map<String,String> replaceMap = new HashMap<>();
        replaceMap.put("${a}","jj");
        replaceMap.put("${b}","kk");
        Assert.assertEquals("aajjbbkkcc",
                getDao.stringReplaces("aa${a}bb${b}cc", replaceMap));
    }

    @Test
    public void camelCaseToHungarianTest() {
        Assert.assertEquals(getDao.camelCaseToHungarian("BackAppTestRunner"), "back_app_test_runner");
    }

    @Test
    public void getFieldNamesTest() {
        List<String> fieldNames = new ArrayList<>();
        for (Field field : User.class.getDeclaredFields())
            fieldNames.add(field.getName());
        Assert.assertEquals(6, fieldNames.size());
    }
}
