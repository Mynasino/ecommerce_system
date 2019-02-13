package com.ecommerce.back.model;

import java.util.Objects;

public class User {
    private int id;
    private String userName;
    private String password;
    private String imgUrl;
    private String salt;
    private String mail;

    public User(){}

    public User(String userName, String password, String imgUrl, String salt, String mail) {
        this.userName = userName;
        this.password = password;
        this.imgUrl = imgUrl;
        this.salt = salt;
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(password, user.password) &&
                Objects.equals(imgUrl, user.imgUrl) &&
                Objects.equals(salt, user.salt) &&
                Objects.equals(mail, user.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, imgUrl, salt, mail);
    }
}
