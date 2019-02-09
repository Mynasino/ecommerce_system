package com.ecommerce.back.service;

import com.ecommerce.back.dao.AdminDAO;
import com.ecommerce.back.model.Admin;
import com.ecommerce.back.util.AdminUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private AdminDAO adminDAO;

    @Autowired
    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public String loginAdmin(String adminName, String password) {
        Admin admin = adminDAO.getAdminByName(adminName);
        if (admin == null)
            return "no admin with this admin name";
        else {
            Admin attemptAdmin = new Admin(adminName, password, admin.getSalt());
            AdminUtil.passwordEncode(attemptAdmin);
            return attemptAdmin.equals(admin) ? "success" : "password not correct";
        }
    }
}
