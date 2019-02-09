package com.ecommerce.back.model;

import java.util.Objects;

public class Admin {
    private int id;
    private String adminName;
    private String password;
    private String salt;

    public Admin(String adminName, String password, String salt) {
        this.adminName = adminName;
        this.password = password;
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passWord) {
        this.password = passWord;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        //return id == admin.id &&
        return Objects.equals(adminName, admin.adminName) &&
                Objects.equals(password, admin.password) &&
                Objects.equals(salt, admin.salt);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(id, adminName, passWord, salt);
        return Objects.hash(adminName, password, salt);
    }
}
