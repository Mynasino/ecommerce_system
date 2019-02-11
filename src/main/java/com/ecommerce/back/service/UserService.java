package com.ecommerce.back.service;

import com.ecommerce.back.dao.ProductCollectionDAO;
import com.ecommerce.back.dao.ProductDAO;
import com.ecommerce.back.dao.UserDAO;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.model.ProductCollection;
import com.ecommerce.back.model.User;
import com.ecommerce.back.util.ImgUtil;
import com.ecommerce.back.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private UserDAO userDAO;
    private ProductCollectionDAO productCollectionDAO;
    private ProductDAO productDAO;

    @Autowired
    public UserService(UserDAO userDAO, ProductCollectionDAO productCollectionDAO, ProductDAO productDAO) {
        this.userDAO = userDAO;
        this.productCollectionDAO = productCollectionDAO;
        this.productDAO = productDAO;
    }

    public String registerUser(RegisterInfo registerInfo) {
        String userName = registerInfo.getUserName();
        String password = registerInfo.getPassword();

        if ((!UserUtil.isLegalUserName(userName)) || (!UserUtil.isLegalPassword(password)))
            return "userName/password is not legal";
        if (userDAO.getUserByUserName(userName) != null)
            return "userName already exist";

        byte[] base64ImgBytes = registerInfo.getImgBase64String().getBytes(StandardCharsets.UTF_8);
        try {
            String newImgUrl = ImgUtil.Base64BytesToLocalImg(base64ImgBytes, registerInfo.getImgType());
            //encode and add user
            User user = new User(userName, password, newImgUrl, UUID.randomUUID().toString().substring(0,5));
            UserUtil.passwordEncode(user);
            userDAO.addUser(user);

            return "success";
        }  catch (Exception e) {
            return e.getMessage();
        }
    }

    public String loginUser(String userName, String password) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null)
            return "no user with this user name";
        else {
            User attemptUser = new User(userName, password, user.getImgUrl(), user.getSalt());
            UserUtil.passwordEncode(attemptUser);
            return attemptUser.equals(user) ? "success" : "password not correct";
        }
    }

    public String modifyPassword(String userName, String password) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null)
            return "no user with this user name";
        else {
            User userWithNewPwd = new User(userName, password, user.getImgUrl(), user.getSalt());
            UserUtil.passwordEncode(userWithNewPwd);
            return (userDAO.updateUserPassword(userWithNewPwd) != 0) ? "success" : "update fail";
        }
    }

    public String updateUser(RegisterInfo registerInfo, int userId) {
        String userName = registerInfo.getUserName();
        String password = registerInfo.getPassword();

        byte[] base64ImgBytes = registerInfo.getImgBase64String().getBytes(StandardCharsets.UTF_8);
        try {
            String newImgUrl = ImgUtil.Base64BytesToLocalImg(base64ImgBytes, registerInfo.getImgType());
            User user = new User(userName, password, newImgUrl, UUID.randomUUID().toString().substring(0,5));
            user.setId(userId);
            UserUtil.passwordEncode(user);
            return (userDAO.updateUser(user)) > 0 ? "success" : "update user " + user.getUserName() + " failed";
        }  catch (Exception e) {
            return e.getMessage();
        }
    }

    public String deleteUser(String userName) {
        User user = userDAO.getUserByUserName(userName);
        return user == null ? "no user with this user name" : (
                    (userDAO.deleteUserByUserName(userName) != 0) ? "success" : "delete fail"
                );
    }

    public User getUserByUserName(String userName) {
        return userDAO.getUserByUserName(userName);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public String collectProduct(String userName, int productId) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalStateException("user name: " + userName + " not exist");

        ProductCollection productCollection = productCollectionDAO.getProductCollectionByUserIdAndProductId(user.getId(), productId);
        if (productCollection != null) throw new IllegalStateException("Repeatable collect not allow");

        productCollectionDAO.addProductCollection(new ProductCollection(-1, user.getId(), productId));
        return "success";
    }

    public String cancelCollectProduct(String userName, int productId) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalStateException("user name: " + userName + " not exist");

        productCollectionDAO.deleteProductCollectionByUserIdAndProductId(user.getId(), productId);
        return "success";
    }

    public List<Product> getProductCollectionByUserName(String userName) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalStateException("user name: " + userName + " not exist");

        List<Integer> productIds = productCollectionDAO.getProductIdsByUserId(user.getId());
        List<Product> products = new ArrayList<>();
        for (int productId : productIds) {
            Product product = productDAO.getProductById(productId);
            if (product != null) products.add(product);
        }

        return products;
    }
}
