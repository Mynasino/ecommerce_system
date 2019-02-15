package com.ecommerce.back.service;

import com.ecommerce.back.dao.AdminDAO;
import com.ecommerce.back.exception.IllegalException;
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

    /**
     * 尝试登陆管理员账户，登陆失败则抛出异常
     * @param adminName 管理员名
     * @param password 密码
     * @throws IllegalException 管理员不存在或密码错误
     */
    public void loginAdmin(String adminName, String password) throws IllegalException {
        Admin admin = adminDAO.getAdminByName(adminName);
        if (admin == null) throw new IllegalException("管理员名", adminName, "不存在");
        Admin attemptAdmin = new Admin(adminName, password, admin.getSalt());
        AdminUtil.passwordEncode(attemptAdmin);
        if (!attemptAdmin.getPassword().equals(admin.getPassword())) throw new IllegalException("管理员密码", password, "错误");
    }
}
