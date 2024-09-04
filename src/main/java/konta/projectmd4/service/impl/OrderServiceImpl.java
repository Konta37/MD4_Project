package konta.projectmd4.service.impl;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormOrder;
import konta.projectmd4.model.entity.*;
import konta.projectmd4.repository.IOrderDetailRepository;
import konta.projectmd4.repository.IOrderRepository;
import konta.projectmd4.repository.IProductRepository;
import konta.projectmd4.service.IOrderService;
import konta.projectmd4.service.admin.impl.ProductService;
import konta.projectmd4.service.user.impl.AddressServiceImpl;
import konta.projectmd4.service.user.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private ProductService productService;

    @Override
    public Order addToOrder(Users users, FormOrder formOrder) throws CustomException {
        // Step 0: find list cart and check
        List<CartItem> cartItems = cartService.getAllCartItems(users);
        if (cartItems == null || cartItems.size() == 0) {
            throw new CustomException("CartItem's user not found!", HttpStatus.NOT_FOUND);
        }

        //Step 0.5: check cart quantity before add to order
        for (CartItem cartItem : cartItems) {
            if (cartItem.getQuantity() > cartItem.getProduct().getQuantity()) {
                throw new CustomException("CartItem's quantity exceeds cartItem quantity! Return to cart to adjust that. product name: " +
                        cartItem.getProduct().getName(), HttpStatus.BAD_REQUEST);
            }
        }

        // Step 1: find address
        Address address = addressService.getAddressById(formOrder.getAddressId(), users.getId());

        // Step 2: find payment
        Payment payment;
        if (formOrder.getPayment().equalsIgnoreCase("CARD") ||
                formOrder.getPayment().equalsIgnoreCase("CAST")) {
            payment = Payment.valueOf(formOrder.getPayment().toUpperCase());
        } else {
            throw new CustomException("You are choosing wrong payment(cast,card)", HttpStatus.NOT_FOUND);
        }

        // Step 3: create new received at date
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        // Add 5 days to the current date
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date receivedAt = calendar.getTime();

        // Step 4: save new order
        Order order = Order.builder()
                .code(UUID.randomUUID().toString().substring(0, 6))
                .status(Status.WAITING)
                .note(formOrder.getNote())
                .receiveAddress(address.getAddress())
                .receivePhone(address.getPhone())
                .receiveName(address.getReceiveName())
                .payment(payment)
                .user(users)
                .createdAt(new Date())
                .receivedAt(receivedAt)
                .build();

        Order newOder = orderRepository.save(order);

        double totalPrice = (double) 0;

        // Step 5: Save to order details
        for (CartItem cartItem : cartItems) {
            // Step 5.1: save order detail
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(cartItem.getProduct())
                    .order(order)
                    .quantity(cartItem.getQuantity())
                    .build();
            orderDetailRepository.save(orderDetail);

            // Step 5.2: calculator total price
            totalPrice = totalPrice + (cartItem.getProduct().getUnitPrice() * cartItem.getQuantity());

            // Step 5.3: set quantity product
            cartItem.getProduct().setQuantity(cartItem.getProduct().getQuantity() - cartItem.getQuantity());
        }

        // Step 6: save new total price
        newOder.setTotalPrice(totalPrice);
        orderRepository.save(newOder);

        // Step 7: delete all cart with users
        cartService.deleteAllByUser(users);

        return newOder;
    }

    @Override
    public Order findOrderByCode(String code, Users users) throws CustomException {
        Order order = orderRepository.getOrderByCodeAndUser(code, users);
        if (order == null) {
            throw new CustomException("Order not found!", HttpStatus.NOT_FOUND);
        }
        return order;
    }

    @Override
    public List<Order> findAllOrders(Users users) throws CustomException {
//        List<Order> orderList =orderRepository.getOrdersByUser(users);
//        if (orderList==null || orderList.size()==0){
//            throw new CustomException("User don't have orders", HttpStatus.NOT_FOUND);
//        }
        return orderRepository.getOrdersByUser(users);
    }

    @Override
    public Order cancelOrder(Users users, Integer orderId) throws CustomException {
        Order order = orderRepository.getOrderByIdAndUser(orderId, users);
        if (order == null) {
            throw new CustomException("Order not found!", HttpStatus.NOT_FOUND);
        }

        if (!order.getStatus().equals(Status.WAITING)) {
            throw new CustomException("Order not waiting! You can't change", HttpStatus.BAD_REQUEST);
        }
        order.setStatus(Status.CANCEL);
        returnQuantityToProduct(order);
        return orderRepository.save(order);
    }
    //WAITING,CONFIRM,DELIVERY,SUCCESS,CANCEL,DENIED
    @Override
    public List<Order> findAllOrdersByStatus(Users users, String status) throws CustomException {
        return switch (status.toUpperCase()) {
            case "WAITING" -> orderRepository.getOrdersByUserAndStatus(users, Status.WAITING);
            case "CONFIRM" -> orderRepository.getOrdersByUserAndStatus(users, Status.CONFIRM);
            case "DELIVERY" -> orderRepository.getOrdersByUserAndStatus(users, Status.DELIVERY);
            case "SUCCESS" -> orderRepository.getOrdersByUserAndStatus(users, Status.SUCCESS);
            case "CANCEL" -> orderRepository.getOrdersByUserAndStatus(users, Status.CANCEL);
            case "DENIED" -> orderRepository.getOrdersByUserAndStatus(users, Status.DENIED);
            default ->
                    throw new CustomException("Order status not found! (WAITING,CONFIRM,DELIVERY,SUCCESS,CANCEL,DENIED)", HttpStatus.NOT_FOUND);
        };
    }

    //admin use

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findAllByStatus(String status) throws CustomException{
        return switch (status.toUpperCase()) {
            case "WAITING" -> orderRepository.getOrdersByStatus(Status.WAITING);
            case "CONFIRM" -> orderRepository.getOrdersByStatus(Status.CONFIRM);
            case "DELIVERY" -> orderRepository.getOrdersByStatus(Status.DELIVERY);
            case "SUCCESS" -> orderRepository.getOrdersByStatus(Status.SUCCESS);
            case "CANCEL" -> orderRepository.getOrdersByStatus(Status.CANCEL);
            case "DENIED" -> orderRepository.getOrdersByStatus(Status.DENIED);
            default ->
                    throw new CustomException("Order status not found! (WAITING,CONFIRM,DELIVERY,SUCCESS,CANCEL,DENIED)", HttpStatus.NOT_FOUND);
        };
    }

    @Override
    public Order findOrderById(Integer orderId) throws CustomException {
        Order order = orderRepository.getOrderById(orderId);
        if (order==null){
            throw new CustomException("Order not found!", HttpStatus.NOT_FOUND);
        }
        return order;
    }

    @Override
    public Order updateStatusByAdmin(Integer orderId, String status) throws CustomException {
        Order order = orderRepository.getOrderById(orderId);

        if (order==null){
            throw new CustomException("Order not found!", HttpStatus.NOT_FOUND);
        }

        if (!(order.getStatus() == Status.WAITING)){
            throw new CustomException("Order status can't be change. It is " + order.getStatus(), HttpStatus.NOT_FOUND);
        }else {
            switch (status.toUpperCase()) {
                case "CONFIRM" -> order.setStatus(Status.CONFIRM);
                case "DELIVERY" -> order.setStatus(Status.DELIVERY);
                case "SUCCESS" -> order.setStatus(Status.SUCCESS);
                case "DENIED" -> {
                    order.setStatus(Status.DENIED);
                    returnQuantityToProduct(order);
                }
                default ->
                        throw new CustomException("Order status not found! (CONFIRM,DELIVERY,SUCCESS,DENIED)", HttpStatus.NOT_FOUND);
            }
            orderRepository.save(order);
        }
        return order;
    }

    @Override
    public void returnQuantityToProduct(Order order){
        List<OrderDetail> orderDetails = orderDetailRepository.getOrderDetailsByOrder(order);
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.getProduct().setQuantity(
                    orderDetail.getProduct().getQuantity() + orderDetail.getQuantity()
            );
            productService.saveNormal(orderDetail.getProduct());
        }
    }
}
