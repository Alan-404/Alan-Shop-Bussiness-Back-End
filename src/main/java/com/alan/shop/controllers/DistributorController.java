package com.alan.shop.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.distributor.RegisterDistributorDTO;
import com.alan.shop.dtos.distributor.ResponseRegisterDistributor;
import com.alan.shop.middleware.DistributorFilter;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Distributor;
import com.alan.shop.models.Product;
import com.alan.shop.services.DistributorService;
import com.alan.shop.services.HadoopService;
import com.alan.shop.services.ProductService;

@RestController
@RequestMapping("/distributor")
public class DistributorController {
    private final DistributorService distributorService;
    private final JwtFilter jwtFilter;
    private final DistributorFilter distributorFilter;
    private final HadoopService hadoopService;
    private final ProductService productService;

    public DistributorController(DistributorService distributorService, HadoopService hadoopService, ProductService productService){
        this.distributorService = distributorService;
        this.jwtFilter = new JwtFilter();
        this.distributorFilter = new DistributorFilter(distributorService);
        this.hadoopService = hadoopService;
        this.productService = productService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterDistributor> registerDistributor(@ModelAttribute RegisterDistributorDTO data, HttpServletRequest httpServletRequest){
        ResponseRegisterDistributor response = new ResponseRegisterDistributor();
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                response.buildMessage("Unauthorization");
                return ResponseEntity.status(400).body(response);
            }

            Distributor checkExisted = this.distributorService.getDistributorByUserId(userId);
            if (checkExisted != null){
                response.buildMessage("You have registered");
                return ResponseEntity.status(400).body(response);
            }

            Distributor distributor = new Distributor();
            distributor.setUserId(userId);
            distributor.setDescription(data.getDescription());

            Distributor savedDistributor = this.distributorService.registerDistributor(distributor);

            if (data.getFile() != null){
                this.hadoopService.saveMedia(data.getFile(), savedDistributor.getId(), "distributors");
            }

            response.buildSuccess(true).buildMessage("Register Successfully").buildDistributor(savedDistributor);
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            System.out.println(exception);
            response.buildMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkDistributor(HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(false);
            }
            Distributor distributor = this.distributorFilter.checkDistributor(userId);
            if (distributor.getId() != null){
                return ResponseEntity.status(200).body(true);
            }
            return ResponseEntity.status(400).body(false);
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(false);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Boolean> checkDistributorProduct(HttpServletRequest httpServletRequest, @PathVariable("id") String id){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(false);
            }
            Product product = this.productService.getProductById(id);

            Distributor distributor = this.distributorService.getDistributorByUserId(userId);
            if (distributor == null ||product == null || product.getDistributorId().equals(distributor.getId()) == false){
                return ResponseEntity.status(400).body(false);
            }

            return ResponseEntity.status(200).body(true);
        }   
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(false);
        }
    }
    
}
