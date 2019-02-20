package com.ecommerce.back.service;

import com.ecommerce.back.dao.OrderDAO;
import com.ecommerce.back.dao.OrderItemDAO;
import com.ecommerce.back.dao.UserDAO;
import com.ecommerce.back.exception.IllegalException;
import com.ecommerce.back.exception.UnauthorizedException;
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
import java.util.concurrent.locks.ReentrantLock;

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

    /**
     * 获得指定用户名的订单列表
     * @param userName 用户名
     * @return 订单列表
     * @throws IllegalException 用户名不存在
     */
    public List<Order> getOrdersOfSpecificUser(String userName) throws IllegalException {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalException("用户名", userName, "不存在");
        return orderDAO.getOrdersByUserId(user.getId());
    }

    /**
     * 根据订单Id获取该订单下所有订单项
     * @param orderId 订单Id
     * @return 订单项列表
     */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        return orderItemDAO.getOrderItemByOrderId(orderId);
    }

    /**
     * 根据用户名创建(如果数据库中没有)或获取(数据库中已存在)状态为购物车的订单
     * @param userName 用户名
     * @return 创建的订单Id
     * @throws IllegalException 信息不合法
     */
    public Order getOrCreateShoppingCartIdByUserName(String userName) throws IllegalException {
        User user = userDAO.getUserByUserName(userName);
        if (user == null) throw new IllegalException("用户名", userName, "不存在");

        Order shoppingCart;
        shoppingCart = orderDAO.getShoppingCartByUserId(user.getId());
        //如果存在购物车，直接返回
        if (shoppingCart != null) return shoppingCart;
        //如果不存在对应用户的购物车，需要锁住对应用户名的ReentrantLock，防止重复创建购物车
        Statistic.userNameLock.putIfAbsent(userName, new Object());
        synchronized (Statistic.userNameLock.get(userName)) {
            shoppingCart = orderDAO.getShoppingCartByUserId(user.getId());
            //仍然不存在购物车，则需要创建购物车
            if (shoppingCart == null) {
                shoppingCart = new Order();
                shoppingCart.setUserId(user.getId());
                shoppingCart.setCreateTime(new Date());
                shoppingCart.setStatusCode(OrderStatus.SHOPPING_CART);
                //生成主键注入到shoppingCart对象
                orderDAO.addOrder(shoppingCart);
            }
        }
        Statistic.userNameLock.remove(userName);

        //返回生成的购物车
        return shoppingCart;
    }

    /**
     * 向购物车中添加商品项，不允许重复添加(通过添加数据库联合主键实现)
     * @param userName 用户名
     * @param shoppingCartId 购物车Id
     * @param productId 商品Id
     * @param count 要购买的数量
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 给定用户不是给定购物车Id的拥有者
     */
    public void addOrderItemToShoppingCart(String userName, int shoppingCartId, int productId, int count) throws IllegalException, UnauthorizedException {
        if (count <= 0) throw new IllegalException("要购买的数量", count + "", "必须大于0");

        //需要对UserName加锁确保添加时给定的用户和购物车Id是合法的合法性
        Statistic.userNameLock.putIfAbsent(userName, new Object());
        synchronized (Statistic.userNameLock.get(userName)) {
            int userId = validationOfUserToShoppingCartId(userName, shoppingCartId);
            orderItemDAO.addOrderItem(new OrderItem(shoppingCartId, productId, count));
        }
        Statistic.userNameLock.remove(userName);
    }

    /**
     * 修改购物车中订单项的购买数量
     * @param userName 用户名
     * @param shoppingCartId 购物车Id
     * @param productId 商品Id
     * @param count 要购买的数量
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 给定用户不是给定购物车Id的拥有者
     */
    public void modifyOrderItemCount(String userName, int shoppingCartId, int productId, int count) throws IllegalException, UnauthorizedException {
        if (count <= 0) throw new IllegalException("要购买的数量", count + "", "必须大于0");

        //需要对UserName加锁确保添加合法性
        Statistic.userNameLock.putIfAbsent(userName, new Object());
        synchronized (Statistic.userNameLock.get(userName)) {
            int userId = validationOfUserToShoppingCartId(userName, shoppingCartId);
            orderItemDAO.updateOrderItemCountByOrderItem(new OrderItem(shoppingCartId, productId, count));
        }
        Statistic.userNameLock.remove(userName);
    }

    /**
     * 删除购物车中的商品项
     * @param userName 用户名
     * @param shoppingCartId 购物车Id
     * @param productId 商品Id
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 给定用户不是给定购物车Id的拥有者
     */
    public void deleteOrderItemInShoppingCart(String userName, int shoppingCartId, int productId) throws IllegalException, UnauthorizedException {
        validationOfUserToShoppingCartId(userName, shoppingCartId);
        orderItemDAO.deleteOrderItemByShoppingCartIdAndProductId(shoppingCartId, productId);
    }

    /**
     * 提交购物车为未支付订单
     * @param userName 用户名
     * @param shoppingCartId 购物车Id
     * @param address 收货地址
     * @param mobile 手机号码
     * @throws IllegalException 信息不合法
     * @throws UnauthorizedException 给定用户不是给定购物车Id的拥有者
     */
    public void submitShoppingCart(String userName, int shoppingCartId, String address, String mobile) throws IllegalException, UnauthorizedException {
        int userId = validationOfUserToShoppingCartId(userName, shoppingCartId);
        orderDAO.updateStatusToPendingPayment(shoppingCartId, address, mobile);
    }

    /**
     * 验证给出的订单Id是否处在购物车状态，且该用户是购物车的拥有者，若不通过验证则抛出异常
     * @param userName 用户名
     * @param orderId 订单Id
     * @throws IllegalException 购物车Id不存在/不是购物车Id/用户Id不存在
     * @throws UnauthorizedException 用户不是购物车的拥有者
     * @return 用户Id
     */
    private int validationOfUserToShoppingCartId(String userName, int orderId) throws IllegalException, UnauthorizedException {
        Order shoppingCart = orderDAO.getOrderById(orderId);
        if (shoppingCart == null) throw new IllegalException("购物车Id", orderId + "", "不存在");
        if (shoppingCart.getStatusCode() != OrderStatus.SHOPPING_CART) throw new IllegalException("订单Id", orderId + "", "不是购物车");

        User user = userDAO.getUserById(shoppingCart.getUserId());
        if (user == null) throw new IllegalException("用户Id", shoppingCart.getUserId() + "", " 不存在");
        if (!user.getUserName().equals(userName)) throw new UnauthorizedException(userName, user.getUserName());

        return user.getId();
    }

    /**
     * 验证对应用户名是否是对应订单的拥有者，若不是则抛出异常
     * @param userName 用户名
     * @param orderId 订单Id
     * @throws IllegalException 订单Id不存在
     * @throws UnauthorizedException 对应用户名不是该订单的拥有者
     */
    private void validationOfUserToOrderId(String userName, int orderId) throws IllegalException, UnauthorizedException {
        Order shoppingCart = orderDAO.getOrderById(orderId);
        if (shoppingCart == null) throw new IllegalException("订单Id", orderId + "", "不存在");

        User user = userDAO.getUserById(shoppingCart.getUserId());
        if (user == null) throw new IllegalException("购物车Id", shoppingCart.getUserId() + "", "不存在");
        if (!user.getUserName().equals(userName)) throw new UnauthorizedException(userName, user.getUserName());
    }
}
