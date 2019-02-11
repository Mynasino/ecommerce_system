package com.ecommerce.back.service;

import com.ecommerce.back.dao.*;
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

    public void addOrderComment(String userName, NewOrderComment newOrderComment) throws IOException, IllegalStateException {
        CommentUtil.validateNewOrderComment(newOrderComment);
        int userId = validateOrderAuthority(userName, newOrderComment.getOrderId());

        if (orderCommentDAO.getOrderCommentIdByOrderId(newOrderComment.getOrderId()) == null) {
            Statistic.userLocks[userId].lock();

            if (orderCommentDAO.getOrderCommentIdByOrderId(newOrderComment.getOrderId()) == null) {
                String[] imgBase64Strings = newOrderComment.getImgBase64Strings();
                String[] imgTypes = newOrderComment.getImgTypes();
                String[] imgUrls = ImgUtil.MultiBase64BytesToLocalImg(imgBase64Strings, imgTypes);

                orderCommentDAO.addOrderComment(new OrderComment(-1, newOrderComment.getContent(), imgUrls,
                        newOrderComment.getScoreLogistics(), newOrderComment.getScoreQuality(),
                        newOrderComment.getScoreService(), newOrderComment.getOrderId()));
            } else
                throw new IllegalStateException("orderComment already exist");

            Statistic.userLocks[userId].unlock();
        } else
            throw new IllegalStateException("orderComment already exist");
    }

    public void addProductComment(String userName, NewProductComment newProductComment) throws IOException, IllegalStateException {
        CommentUtil.validateNewProductComment(newProductComment);
        int userId = validateProductCommentAuthority(userName, newProductComment.getOrderId(), newProductComment.getProductId());
        int orderId = newProductComment.getOrderId();
        int productId = newProductComment.getProductId();

        if (productCommentDAO.getProductCommentIdByOrderAndProductId(orderId, productId) == null) {
            Statistic.userLocks[userId].lock();

            if (productCommentDAO.getProductCommentIdByOrderAndProductId(orderId, productId) == null) {
                String[] imgBase64Strings = newProductComment.getImgBase64Strings();
                String[] imgTypes = newProductComment.getImgTypes();
                String[] imgUrls = ImgUtil.MultiBase64BytesToLocalImg(imgBase64Strings, imgTypes);

                productCommentDAO.addProductComment(new ProductComment(-1, productId, userId, newProductComment.getContent(),
                        imgUrls, newProductComment.getScore(), new Date(), orderId));
            } else
                throw new IllegalStateException("productComment already exist");

            Statistic.userLocks[userId].unlock();
        } else
            throw new IllegalStateException("productComment already exist");
    }

    public OrderComment getOrderComment(String userName, int orderId) {
        validateOrderAuthority(userName, orderId);

        return orderCommentDAO.getOrderCommentByOrderId(orderId);
    }

    public OrderComment getOrderCommentByOrderId(String userName, int orderId) {
        validateOrderAuthority(userName, orderId);

        return orderCommentDAO.getOrderCommentByOrderId(orderId);
    }

    public List<ProductComment> getProductCommentsByUserName(String userName) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalStateException("user " + userName + " don't exist");

        return productCommentDAO.getProductCommentByUserId(user.getId());
    }

    public List<ProductComment> getProductCommentsByProductId(int productId, int limit, int offset) {
        return productCommentDAO.getProductCommentByProductId(productId, limit, offset);
    }

    private int validateOrderAuthority(String userName, int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) throw new IllegalStateException("order with id: " + orderId + " not exist");

        User user = userDAO.getUserById(order.getUserId());
        if (user == null) throw new IllegalStateException("user name " + userName + " not exist");
        if (!user.getUserName().equals(userName)) throw new IllegalStateException("user " + userName + " try visit " + user.getUserName());

        return user.getId();
    }

    private int validateProductCommentAuthority(String userName, int orderId, int productId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) throw new IllegalStateException("order with id: " + orderId + " not exist");

        User user = userDAO.getUserById(order.getUserId());
        if (user == null) throw new IllegalStateException("user name " + userName + " not exist");
        if (!user.getUserName().equals(userName)) throw new IllegalStateException("user " + userName + " try visit " + user.getUserName());

        OrderItem orderItem = orderItemDAO.getOrderItemByOrderIdAndProductId(order.getId(), productId);
        if (orderItem == null) throw new IllegalStateException("orderItem with orderId:" + orderId + " productId:" + productId + " not exist");

        return user.getId();
    }
}
