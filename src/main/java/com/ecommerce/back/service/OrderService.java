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

        int userId = user.getId();
        //Dual lock guarantees no repeat of creating a shopping cart
        Integer shoppingCartId;
        if ((shoppingCartId = orderDAO.getShoppingCartOrderIdByUserId(user.getId())) == null) {
            Statistic.userLocks[userId].lock();
            if (orderDAO.getShoppingCartOrderIdByUserId(user.getId()) == null) {
                Order newShoppingCart = new Order();
                newShoppingCart.setUserId(user.getId());
                newShoppingCart.setCreateTime(new Date());
                newShoppingCart.setStatusCode(OrderStatus.SHOPPING_CART);

                shoppingCartId = orderDAO.addOrder(newShoppingCart);
            }
            Statistic.userLocks[userId].unlock();
        }
        return shoppingCartId;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public String addOrderItemToShoppingCart(String userName, int shoppingCartId, int productId, int count) {
        if (count <= 0) return "count must be > 0";
        int userId = validationOfUserToShoppingCartId(userName, shoppingCartId);

        //not allow repeatable add of same orderItem to same shoppingCart
        if (orderItemDAO.getOrderItemByOrderIdAndProductId(shoppingCartId, productId) == null) {
            Statistic.userLocks[userId].lock();
            if (orderItemDAO.getOrderItemByOrderIdAndProductId(shoppingCartId, productId) == null) {
                orderItemDAO.addOrderItem(new OrderItem(-1, shoppingCartId, productId, count));
            } else
                throw new IllegalStateException("orderItem with productId: " +  productId + " and shoppingCartId: " + shoppingCartId + " already exist");
            Statistic.userLocks[userId].unlock();
        } else
            throw new IllegalStateException("orderItem with productId: " +  productId + " and shoppingCartId: " + shoppingCartId + " already exist");

        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public String modifyOrderItemCount(String userName, int shoppingCartId, int productId, int count) {
        if (count <= 0) return "count must be > 0";
        int userId = validationOfUserToShoppingCartId(userName, shoppingCartId);

        OrderItem originOrderItem = orderItemDAO.getOrderItemByOrderIdAndProductId(shoppingCartId, productId);
        if (originOrderItem == null) return "orderItem with productId: " +  productId + " and shoppingCartId: " + shoppingCartId + " not exist";
        return orderItemDAO.updateOrderItemCountById(originOrderItem.getId(), count) > 0 ? "success" : "failed to modify count of OrderItem " + originOrderItem.getId();
    }

    public String deleteOrderItemInShoppingCart(String userName, int shoppingCartId, int productId) {
        int userId = validationOfUserToShoppingCartId(userName, shoppingCartId);

        orderItemDAO.deleteOrderItemByShoppingCartIdAndProductId(shoppingCartId, productId);
        return "success";
    }

    public String submitShoppingCart(String userName, int shoppingCartId, String address, String mobile) {
        int userId = validationOfUserToShoppingCartId(userName, shoppingCartId);

        orderDAO.updateStatusToPendingPayment(shoppingCartId, address, mobile);
        return "success";
    }

    private int validationOfUserToShoppingCartId(String userName, int orderId) {
        Order shoppingCart = orderDAO.getOrderById(orderId);
        if (shoppingCart == null) throw new IllegalStateException("ShoppingCart id " + orderId + " not exist");
        if (shoppingCart.getStatusCode() != OrderStatus.SHOPPING_CART) throw new IllegalStateException("offered id is not shoppingCart id");

        User user = userDAO.getUserById(shoppingCart.getUserId());
        if (user == null) throw new IllegalStateException("User id " + shoppingCart.getUserId() + " not exist");
        if (!user.getUserName().equals(userName)) throw new IllegalStateException("user " + userName + " try modify " + "orderItem to " + user.getUserName() + "'s shoppingCart");

        return user.getId();
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
