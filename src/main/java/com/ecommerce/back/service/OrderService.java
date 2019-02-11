package com.ecommerce.back.service;

import com.ecommerce.back.dao.OrderDAO;
import com.ecommerce.back.dao.OrderItemDAO;
import com.ecommerce.back.dao.UserDAO;
import com.ecommerce.back.model.Order;
import com.ecommerce.back.model.OrderItem;
import com.ecommerce.back.model.OrderStatus;
import com.ecommerce.back.model.User;
import com.ecommerce.back.statistic.Statistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Autowired
    public OrderService(UserDAO userDAO, OrderDAO orderDAO, OrderItemDAO orderItemDAO) {
        this.userDAO = userDAO;
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
    }

    public List<Order> getOrdersOfSpecificUser(String userName) {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) {
            logger.warn("Authentication pass of user that don't exist");
            return null;
        }
        else
            return orderDAO.getOrdersByUserId(user.getId());
    }

    public List<OrderItem> getOrderItemsByOrderId(String userName, int orderId) {
        String info = validationOfUserToOrderId(userName, orderId);
        if (!info.equals("success")) throw new IllegalStateException(info);

        return orderItemDAO.getOrderItemByOrderId(orderId);
    }

    public Integer getOrCreateShoppingCartIdByUserName(String userName) throws IllegalStateException {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) {
            logger.warn("Authentication pass of user that don't exist");
            return null;
        }

        Integer shoppingCartId = orderDAO.getShoppingCartOrderIdByUserId(user.getId());
        //Dual lock guarantees no repeat of creating a shopping cart
        //use a HashMap maintained by LoginController and StatisticListener
        if (shoppingCartId == null) {
            Date JWTExpiredDate = Statistic.onlineUsers.get(user.getUserName());
            if (JWTExpiredDate == null) throw new IllegalStateException("JWT expired, please relogin"); //JWT has expired
            synchronized (JWTExpiredDate) { //otherwise lock JWTExpiredDate
                shoppingCartId = orderDAO.getShoppingCartOrderIdByUserId(user.getId());
                if (shoppingCartId == null) {
                    Order newShoppingCart = new Order();
                    newShoppingCart.setUserId(user.getId());
                    newShoppingCart.setCreateTime(new Date());
                    newShoppingCart.setStatusCode(OrderStatus.SHOPPING_CART);

                    shoppingCartId = orderDAO.addOrder(newShoppingCart);
                }
            }
        }

        return shoppingCartId;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public String addOrderItemToShoppingCart(String userName, int shoppingCartId, int productId, int count) {
        if (count <= 0) return "count must be > 0";
        String info = validationOfUserToShoppingCartId(userName, shoppingCartId);
        if (!info.equals("success")) return info;

        //not allow repeatable add of same orderItem to same shoppingCart
        if (orderItemDAO.getOrderItemByOrderIdAndProductId(shoppingCartId, productId) != null)
            return "orderItem with productId: " +  productId + " and shoppingCartId: " + shoppingCartId + " already exist";
        orderItemDAO.addOrderItem(new OrderItem(-1, shoppingCartId, productId, count));
        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public String modifyOrderItemCount(String userName, int shoppingCartId, int productId, int count) {
        if (count <= 0) return "count must be > 0";
        String info = validationOfUserToShoppingCartId(userName, shoppingCartId);
        if (!info.equals("success")) return info;

        OrderItem originOrderItem = orderItemDAO.getOrderItemByOrderIdAndProductId(shoppingCartId, productId);
        if (originOrderItem == null) return "orderItem with productId: " +  productId + " and shoppingCartId: " + shoppingCartId + " not exist";
        return orderItemDAO.updateOrderItemCountById(originOrderItem.getId(), count) > 0 ? "success" : "failed to modify count of OrderItem " + originOrderItem.getId();
    }

    public String deleteOrderItemInShoppingCart(String userName, int shoppingCartId, int productId) {
        String info = validationOfUserToShoppingCartId(userName, shoppingCartId);
        if (!info.equals("success")) return info;

        orderItemDAO.deleteOrderItemByShoppingCartIdAndProductId(shoppingCartId, productId);
        return "success";
    }

    public String submitShoppingCart(String userName, int shoppingCartId, String address, String mobile) {
        String info = validationOfUserToShoppingCartId(userName, shoppingCartId);
        if (!info.equals("success")) return info;

        orderDAO.updateStatusToPendingPayment(shoppingCartId, address, mobile);
        return "success";
    }

    private String validationOfUserToShoppingCartId(String userName, int orderId) {
        Order shoppingCart = orderDAO.getOrderById(orderId);
        if (shoppingCart == null) return "ShoppingCart id " + orderId + " not exist";
        if (shoppingCart.getStatusCode() != OrderStatus.SHOPPING_CART) return "offered id is not shoppingCart id";
        User user = userDAO.getUserById(shoppingCart.getUserId());
        if (user == null) return "User id " + shoppingCart.getUserId() + " not exist";
        if (!user.getUserName().equals(userName)) return "user " + userName + " try modify " + "orderItem to " + user.getUserName() + "'s shoppingCart";

        return "success";
    }

    private String validationOfUserToOrderId(String userName, int orderId) {
        Order shoppingCart = orderDAO.getOrderById(orderId);
        if (shoppingCart == null) return "Order id " + orderId + " not exist";
        User user = userDAO.getUserById(shoppingCart.getUserId());
        if (user == null) return "User id " + shoppingCart.getUserId() + " not exist";
        if (!user.getUserName().equals(userName)) return "user " + userName + " try visit " + "orderItem to " + user.getUserName() + "'s order";

        return "success";
    }
}
