package com.ecommerce.back.service;

import com.ecommerce.back.dao.*;
import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.exception.UnauthorizedException;
import com.ecommerce.back.jsonInfo.NewOrderComment;
import com.ecommerce.back.jsonInfo.NewProductComment;
import com.ecommerce.back.model.*;
import com.ecommerce.back.statistic.Statistic;
import com.ecommerce.back.util.CommentUtil;
import com.ecommerce.back.util.ImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CommentService {
    private OrderCommentDAO orderCommentDAO;
    private ProductCommentDAO productCommentDAO;
    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Autowired
    public CommentService(OrderCommentDAO orderCommentDAO, ProductCommentDAO productCommentDAO, UserDAO userDAO, OrderDAO orderDAO, OrderItemDAO orderItemDAO) {
        this.orderCommentDAO = orderCommentDAO;
        this.productCommentDAO = productCommentDAO;
        this.userDAO = userDAO;
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
    }

    /**
     * 添加对订单的评价
     * @param userName 用户名
     * @param newOrderComment 新的订单评论
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 未具有权限
     * @throws IOException 上传图片IOException
     */
    public void addOrderComment(String userName, NewOrderComment newOrderComment) throws IllegalException, UnauthorizedException, IOException {
        CommentUtil.validateNewOrderComment(newOrderComment);
        int userId = validateOrderAuthority(userName, newOrderComment.getOrderId());

        //需要锁住UserName对应的锁，防止重复添加
        Statistic.userNameLock.putIfAbsent(userName, new ReentrantLock());
        ReentrantLock reentrantLock = Statistic.userNameLock.get(userName);
        reentrantLock.lock();

        if (orderCommentDAO.getOrderCommentIdByOrderId(newOrderComment.getOrderId()) != null)
            throw new IllegalException("OrderId的评论", newOrderComment.getOrderId() + "", "已存在");
        //还未有相应评论，开始上传图片
        String[] imgBase64Strings = newOrderComment.getImgBase64Strings();
        String[] imgTypes = newOrderComment.getImgTypes();
        String[] imgUrls = ImgUtil.MultiBase64StringsToLocalImg(imgBase64Strings, imgTypes);
        //对订单添加评论
        orderCommentDAO.addOrderComment(new OrderComment(-1, newOrderComment.getContent(), imgUrls,
                newOrderComment.getScoreLogistics(), newOrderComment.getScoreQuality(),
                newOrderComment.getScoreService(), newOrderComment.getOrderId()));

        reentrantLock.unlock();
        Statistic.userNameLock.remove(userName);
    }

    /**
     * 添加某订单下对商品的评论
     * @param userName 用户名
     * @param newProductComment 新的商品评论
     * @throws IOException 图片上传IOException
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 不具有相应权限
     */
    public void addProductComment(String userName, NewProductComment newProductComment) throws IOException, IllegalException, UnauthorizedException {
        CommentUtil.validateNewProductComment(newProductComment);
        int userId = validateProductCommentAuthority(userName, newProductComment.getOrderId(), newProductComment.getProductId());
        int orderId = newProductComment.getOrderId();
        int productId = newProductComment.getProductId();

        //需要锁住对应UserName的ReentrantLock
        Statistic.userNameLock.putIfAbsent(userName, new ReentrantLock());
        ReentrantLock reentrantLock = Statistic.userNameLock.get(userName);
        reentrantLock.lock();

        if (productCommentDAO.getProductCommentIdByOrderAndProductId(orderId, productId) != null)
            throw new IllegalException("商品评论", orderId + " " + productId, "已存在");
        //不存在该订单下的该商品的评论，开始上传图片
        String[] imgBase64Strings = newProductComment.getImgBase64Strings();
        String[] imgTypes = newProductComment.getImgTypes();
        String[] imgUrls = ImgUtil.MultiBase64StringsToLocalImg(imgBase64Strings, imgTypes);
        //添加商品评论
        productCommentDAO.addProductComment(new ProductComment(-1, productId, userId, newProductComment.getContent(),
                imgUrls, newProductComment.getScore(), new Date(), orderId));

        reentrantLock.unlock();
        Statistic.userNameLock.remove(userName);
    }

    /**
     * 返回指定用户对指定订单的评价
     * @param userName 用户名
     * @param orderId 订单Id
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 不具有相应权限
     * @return 指定用户对指定订单的评价
     */
    public OrderComment getOrderComment(String userName, int orderId) throws IllegalException, UnauthorizedException {
        validateOrderAuthority(userName, orderId);
        return orderCommentDAO.getOrderCommentByOrderId(orderId);
    }

    /**
     * 返回指定用户对所有商品的评价
     * @param userName 用户名
     * @throws IllegalException 用户名不存在
     * @return 商品评价列表
     */
    public List<ProductComment> getProductCommentsByUserName(String userName) throws IllegalException {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalException("用户名", userName, "不存在");

        return productCommentDAO.getProductCommentByUserId(user.getId());
    }

    /**
     * 返回指定商品的从offset个开始数limit个评价
     * @param productId 商品Id
     * @param limit 返回的商品评价最大数量
     * @param offset 从第offset条评价开始取
     * @return 商品评价列表
     */
    public List<ProductComment> getProductCommentsByProductId(int productId, int limit, int offset) {
        return productCommentDAO.getProductCommentByProductId(productId, limit, offset);
    }

    /**
     * 验证给定用户名是否拥有给定订单Id的权限
     * @param userName 用户名
     * @param orderId 订单Id
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 未拥有权限
     * @return 用户Id
     */
    private int validateOrderAuthority(String userName, int orderId) throws IllegalException, UnauthorizedException {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) throw new IllegalException("订单Id", orderId + "", "不存在");

        User user = userDAO.getUserById(order.getUserId());
        if (user == null) throw new IllegalException("用户Id", userName + "", "不存在");
        if (!user.getUserName().equals(userName)) throw new UnauthorizedException(userName ,user.getUserName());

        return user.getId();
    }

    /**
     * 验证给定用户名是否购买过该订单下的该商品
     * @param userName 用户名
     * @param orderId 订单Id
     * @param productId 商品Id
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 未拥有权限
     * @return 用户Id
     */
    private int validateProductCommentAuthority(String userName, int orderId, int productId) throws IllegalException, UnauthorizedException {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) throw new IllegalException("订单Id", orderId + "", "不存在");

        User user = userDAO.getUserById(order.getUserId());
        if (user == null) throw new IllegalException("用户名", userName + "", "不存在");
        if (!user.getUserName().equals(userName)) throw new UnauthorizedException(userName, user.getUserName());

        OrderItem orderItem = orderItemDAO.getOrderItemByOrderIdAndProductId(order.getId(), productId);
        if (orderItem == null) throw new IllegalException("未在订单Id" + orderId + "购买过的商品Id", productId + "", "");

        return user.getId();
    }
}
