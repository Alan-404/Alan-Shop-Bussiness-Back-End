package com.alan.shop.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.warehouse.ActionWarehouseDTO;
import com.alan.shop.dtos.warehouse.ResponseActionWarehouse;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Distributor;
import com.alan.shop.models.HistoryWarehouse;
import com.alan.shop.models.Product;
import com.alan.shop.models.Warehouse;
import com.alan.shop.services.DistributorService;
import com.alan.shop.services.HistoryWarehouseService;
import com.alan.shop.services.ProductService;
import com.alan.shop.services.WarehouseService;



@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final HistoryWarehouseService historyWarehouseService;
    private final ProductService productService;
    private final DistributorService distributorService;
    private final JwtFilter jwtFilter;

    public WarehouseController(WarehouseService warehouseService, HistoryWarehouseService historyWarehouseService, ProductService productService, DistributorService distributorService){
        this.warehouseService = warehouseService;
        this.historyWarehouseService = historyWarehouseService;
        this.productService = productService;
        this.distributorService = distributorService;
        this.jwtFilter = new JwtFilter();
    }

    @PutMapping("/change")
    public ResponseEntity<ResponseActionWarehouse> actionWarehouse(@RequestBody ActionWarehouseDTO data, HttpServletRequest httpServletRequest){
        ResponseActionWarehouse response = new ResponseActionWarehouse();
        try{

            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                response.buildMessgae("Unauthorization");
                return ResponseEntity.status(400).body(response);
            }

            Product product = this.productService.getProductById(data.getProductId());
            if (product == null){
                response.buildMessgae("Not Found Product");
                return ResponseEntity.status(404).body(response);
            }

            Distributor distributor = this.distributorService.getDistributorByUserId(userId);
            if (distributor == null){
                response.buildMessgae("You are not distributor");
                return ResponseEntity.status(400).body(response);
            }

            if (distributor.getId().equals(product.getDistributorId()) == false){
                response.buildMessgae("You are not distributor of this product");
                return ResponseEntity.status(400).body(response);
            }

            Warehouse warehouse = this.warehouseService.getWarehouseByProduct(data.getProductId());
            if (warehouse == null){
                response.setMessage("Unknow Data");
                return ResponseEntity.status(404).body(response);
            }

            Warehouse newWarehouse = this.warehouseService.changeWarehouse(warehouse, data.getQuantity(), data.isType());

            this.warehouseService.saveData(newWarehouse);

            HistoryWarehouse history = new HistoryWarehouse();
            history.setProductId(warehouse.getProductId());
            history.setQuantity(data.getQuantity());
            history.setType(data.isType());
            this.historyWarehouseService.saveHistory(history);

            response.buildMessgae("Saved").buildSuccess(true);
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            response.buildMessgae("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }
}
