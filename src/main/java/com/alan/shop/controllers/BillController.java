package com.alan.shop.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.Response;
import com.alan.shop.dtos.bill.CreateBillDTO;
import com.alan.shop.dtos.bill.PaginationBills;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Bill;
import com.alan.shop.models.Order;
import com.alan.shop.models.Product;
import com.alan.shop.models.User;
import com.alan.shop.models.Warehouse;
import com.alan.shop.services.BillService;
import com.alan.shop.services.CartService;
import com.alan.shop.services.OrderService;
import com.alan.shop.services.ProductService;
import com.alan.shop.services.UserService;
import com.alan.shop.services.WarehouseService;
import com.alan.shop.unions.BillDetail;
import com.alan.shop.unions.CartProductDetail;
import com.alan.shop.unions.InfoBill;
import com.alan.shop.unions.InfoOrder;


@RestController
@RequestMapping("/bill")
public class BillController {
    private final BillService billService;
    private final JwtFilter jwtFilter;
    private final OrderService orderService;
    private final CartService cartService;
    private final WarehouseService warehouseService;
    private final UserService userService;
    private final ProductService productService;

    public BillController(BillService billService, OrderService orderService, CartService cartService, WarehouseService warehouseService, UserService userService, ProductService productService){
        this.billService = billService;
        this.jwtFilter = new JwtFilter();
        this.cartService = cartService;
        this.orderService = orderService;
        this.warehouseService = warehouseService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Response> saveBill(@RequestBody CreateBillDTO data, HttpServletRequest httpServletRequest){
        try{   
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(new Response(false, "Unauthorization"));
            }

            double price = 0;
            List<Order> orders = new ArrayList<>();
            for (CartProductDetail item : data.getProducts()) {
                double discount = (100 - item.getDiscount().getValue())/100;
                price += item.getProduct().getPrice()*(discount)*(item.getCart().getQuantity());
                Order order = new Order();
                order.setPrice(item.getProduct().getPrice()*(discount));
                order.setProductId(item.getProduct().getId());
                order.setQuantity(item.getCart().getQuantity());
                orders.add(order);
                this.cartService.deleteCart(item.getCart().getId());
                Warehouse warehouse = this.warehouseService.getWarehouseByProduct(item.getProduct().getId());
                this.warehouseService.changeWarehouse(warehouse, item.getCart().getQuantity(), false);
            }
            Bill bill = new Bill();
            bill.setPrice(price);
            bill.setUserId(userId);
            bill.setOrderTime(new Timestamp(System.currentTimeMillis()));
            Bill savedBill = this.billService.saveBill(bill);

            for (Order order : orders) {
                order.setBillId(savedBill.getId());
                this.orderService.saveOrder(order);
            }

            return ResponseEntity.status(200).body(new Response(true, "Saved Bill"));

        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(new Response(false, "Internal Error Server"));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<PaginationBills> getMyBills(HttpServletRequest httpServletRequest , @RequestParam(name = "page" , required = false) Integer page, @RequestParam(name = "num", required = false) Integer num){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(null);
            }

            if (page == null){
                page = 1;
            }

           int totalBills = this.billService.getBillsByUser(userId).size();

            if (num == null){
                num = this.billService.getBillsByUser(userId).size();
            }

            int totalPages = totalBills/num;
            if (totalBills % num != 0){
                totalPages++;
            }

            List<Bill> bills = this.billService.paginateBillsByUser(userId, page, num);
            List<InfoBill> items = new ArrayList<>();
            for (Bill bill : bills) {
                InfoBill item = new InfoBill();
                item.setBill(bill);
                item.setOrders(this.orderService.getOrdersByBill(bill.getId()));
                item.setUser(this.userService.getUserById(bill.getUserId()));
                items.add(item);
            }

            return ResponseEntity.status(200).body(new PaginationBills(items, totalPages, totalBills));
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDetail> getDetailBill(@PathVariable("id") String billId, HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(null);
            }

            Bill bill = this.billService.getBillById(billId);
            if (bill == null || bill.getUserId().equals(userId) == false){
                return ResponseEntity.status(400).body(null);
            }

            BillDetail info = new BillDetail();
            info.setBill(bill);
            
            List<InfoOrder> orders = new ArrayList<>();

            List<Order> orderItems = this.orderService.getOrdersByBill(billId);
            for (Order orderItem : orderItems) {
                Product product = this.productService.getProductById(orderItem.getProductId());
                InfoOrder item = new InfoOrder(orderItem, product);
                orders.add(item);
            }

            info.setOrders(orders);

            User user = this.userService.getUserById(userId);
            info.setUser(user);

            return ResponseEntity.status(200).body(info);
            
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }
}
