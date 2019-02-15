package com.ecommerce.back.service;

import com.ecommerce.back.dao.ProductCollectionDAO;
import com.ecommerce.back.dao.ProductDAO;
import com.ecommerce.back.dao.UserDAO;
import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.jsonInfo.RegisterInfo;
import com.ecommerce.back.model.Product;
import com.ecommerce.back.model.ProductCollection;
import com.ecommerce.back.model.User;
import com.ecommerce.back.statistic.Statistic;
import com.ecommerce.back.util.ImgUtil;
import com.ecommerce.back.util.MailUtil;
import com.ecommerce.back.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

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

    /**
     * 检查注册用户所需信息(RegisterInfo)的合法性，合法则返回User实例，不合法则抛出异常
     * @param registerInfo 注册一个用户所需的信息
     * @throws IllegalException 信息不合法
     * @throws IOException 上传图片IOException
     */
    private User checkRegisterInfoAndBuildUser(RegisterInfo registerInfo) throws IllegalException, IOException {
        String userName = registerInfo.getUserName();
        String password = registerInfo.getPassword();
        String mailAddress = registerInfo.getMail();
        String imgBase64String = registerInfo.getImgBase64String();

        //检查注册信息是否合法
        UserUtil.checkUserNameLegality(userName);
        UserUtil.checkLegalPasswordLegality(password);
        MailUtil.checkMailAddLegality(mailAddress);
        ImgUtil.checkImgBase64String(imgBase64String, 50000);

        //如果当前用户名未重复，则将图片的Base64编码字符串上传并得到返回的图片URL
        if (userDAO.getUserIdByUserName(userName) != null) throw new IllegalException("用户名", userName, "用户名已存在");
        String newImgUrl = ImgUtil.Base64BytesToLocalImg(
                imgBase64String.getBytes(StandardCharsets.UTF_8),
                registerInfo.getImgType()
        );

        //构造用户实例，将明文密码加密
        User user = new User(userName, password, newImgUrl, UUID.randomUUID().toString().substring(0,5), mailAddress);
        UserUtil.passwordEncode(user);

        return user;
    }

    /**
     * 注册一个用户，不允许用户名重复
     * @param registerInfo 注册一个用户所需的信息
     * @throws IllegalException 信息不合法
     * @throws IOException 上传图片IOException
     */
    public void registerUser(RegisterInfo registerInfo) throws IllegalException, IOException {
        //检查注册用户所需信息的合法性
        User user = checkRegisterInfoAndBuildUser(registerInfo);

        String userName = user.getUserName();
        //对UserName放入对应的锁
        Statistic.userNameLock.putIfAbsent(userName,new ReentrantLock());
        ReentrantLock reentrantLock = Statistic.userNameLock.get(userName);
        //尝试注册用户，需要锁住userName对应的ReentrantLock
        reentrantLock.lock();
        if (userDAO.getUserIdByUserName(userName) != null)
            throw new IllegalException("用户名", userName, "用户名已存在");
        else
            userDAO.addUser(user);
        reentrantLock.unlock();
        //成功注册用户后，无需该锁，因为后续进程在访问数据库时可得到用户名已存在
        Statistic.userNameLock.remove(userName);
    }

    /**
     * 用户登陆
     * @param userName 用户名
     * @param password 密码
     * @throws IllegalException 抛出异常说明登陆失败
     */
    public void loginUser(String userName, String password) throws IllegalException {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalException("用户名", userName, "用户名不存在");
        //通过查到对应用户的盐和提交的密码，构造User实例并将明文密码转为密文密码
        User attemptUser = new User(userName, password, user.getImgUrl(), user.getSalt(), user.getMail());
        UserUtil.passwordEncode(attemptUser);
        if (!attemptUser.getPassword().equals(user.getPassword()))
            throw new IllegalException("密码",password,"密码不正确");
    }

    /**
     * 根据用户名和新密码更新对应用户的密码
     * @param userName 用户名
     * @param password 新密码
     * @throws IllegalException 用户名不存在
     */
    public void modifyPassword(String userName, String password) throws IllegalException {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalException("用户名", userName, "用户名不存在");
        //构建User实例,加密密码并更新到数据库
        User userWithNewPwd = new User(userName, password, user.getImgUrl(), user.getSalt(), user.getMail());
        UserUtil.passwordEncode(userWithNewPwd);
        userDAO.updateUserPassword(userWithNewPwd);
    }

    /**
     * 用注册所需信息对给定的用户Id进行更新，不允许重复用户名
     * @param registerInfo 注册所需信息
     * @param userId 用户在数据库里的Id
     * @throws IllegalException 注册信息不合法
     * @throws IOException 上传图片IOException
     */
    public void updateUser(RegisterInfo registerInfo, int userId) throws IllegalException, IOException {
        //检查注册用户所需信息的合法性
        User user = checkRegisterInfoAndBuildUser(registerInfo);

        String userName = user.getUserName();
        //对UserName放入对应的锁
        Statistic.userNameLock.putIfAbsent(userName,new ReentrantLock());
        ReentrantLock reentrantLock = Statistic.userNameLock.get(userName);
        //尝试更新用户，需要锁住userName对应的ReentrantLock
        reentrantLock.lock();
        if (userDAO.getUserIdByUserName(userName) != null)
            throw new IllegalException("用户名", userName, "用户名已存在");
        user.setId(userId);
        userDAO.updateUser(user);
        reentrantLock.unlock();
        //成功注册用户后，无需该锁，因为后续进程在访问数据库时可得到用户名已存在
        Statistic.userNameLock.remove(userName);
    }

    /**
     * 根据用户名来删除用户
     * @param userName 需要删除的用户的用户名
     */
    public void deleteUser(String userName) {
        userDAO.deleteUserByUserName(userName);
    }

    /**
     * 根据用户名来查询用户
     * @param userName 用户名
     */
    public User getUserByUserName(String userName) throws IllegalException {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalException("用户名",userName,"用户名不存在");
        //不允许访问盐
        user.setSalt("not visitable");
        return user;
    }

    /**
     * 根据给定的用户Name和商品Id来收藏商品
     * 数据库内加级联外键约束防止无效收藏
     * 将用户Id和商品Id作为联合主键来防止重复收藏
     * @param userName 用户Name
     * @param productId 商品Id
     * @throws IllegalException 用户名不存在
     * @throws DataAccessException 无效收藏/重复收藏/其他
     */
    public void collectProduct(String userName, int productId) throws IllegalException, DataAccessException {
        Integer userId = userDAO.getUserIdByUserName(userName);
        if (userId == null) throw new IllegalException("用户名",userName,"用户名不存在");
        productCollectionDAO.addProductCollection(new ProductCollection(userId, productId));
    }

    /**
     * 根据给定的用户名和商品Id来取消收藏
     * @param userName 用户名
     * @param productId 商品Id
     * @throws IllegalException 用户名不存在
     */
    public void cancelCollectProduct(String userName, int productId) throws IllegalException {
        Integer userId = userDAO.getUserIdByUserName(userName);
        if (userId == null) throw new IllegalException("用户名",userName,"所查找的用户名不存在");
        productCollectionDAO.deleteProductCollectionByUserIdAndProductId(userId, productId);
    }

    /**
     * 根据给定的用户名来获得收藏的所有商品的信息
     * @param userName 用户名
     */
    public List<Product> getProductCollectionByUserName(String userName) throws IllegalException {
        Integer userId = userDAO.getUserIdByUserName(userName);
        if (userId == null) throw new IllegalException("用户名",userName,"所查找的用户名不存在");

        List<Integer> productIds = productCollectionDAO.getProductIdsByUserId(userId);
        List<Product> products = new ArrayList<>();
        for (int productId : productIds) {
            Product product = productDAO.getProductById(productId);
            if (product != null) products.add(product);
        }

        return products;
    }
}
