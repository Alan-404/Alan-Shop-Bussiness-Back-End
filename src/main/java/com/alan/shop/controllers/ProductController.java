package com.alan.shop.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.Response;
import com.alan.shop.dtos.product.AddProductDTO;
import com.alan.shop.dtos.product.PaginationDetailProduct;
import com.alan.shop.dtos.product.ResponseAddProduct;
import com.alan.shop.dtos.product.ResponsePaginationProducts;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Category;
import com.alan.shop.models.CategoryItem;
import com.alan.shop.models.Discount;
import com.alan.shop.models.Distributor;
import com.alan.shop.models.Product;
import com.alan.shop.models.Warehouse;
import com.alan.shop.services.CategoryItemService;
import com.alan.shop.services.CategoryService;
import com.alan.shop.services.DiscountService;
import com.alan.shop.services.DistributorService;
import com.alan.shop.services.HadoopService;
import com.alan.shop.services.ProductService;
import com.alan.shop.services.WarehouseService;
import com.alan.shop.unions.InfoProduct;
import com.alan.shop.utils.Constants;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final DiscountService discountService;
    private final WarehouseService warehouseService;
    private final DistributorService distributorService;
    private final JwtFilter jwtFilter;
    private final HadoopService hadoopService;
    private final ModelMapper modelMapper;
    private final CategoryItemService categoryItemService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, DiscountService discountService, WarehouseService warehouseService, DistributorService distributorService, HadoopService hadoopService, CategoryItemService categoryItemService, CategoryService categoryService){
        this.productService = productService;
        this.discountService = discountService;
        this.warehouseService = warehouseService;
        this.distributorService = distributorService;
        this.jwtFilter = new JwtFilter();
        this.hadoopService = hadoopService;
        this.modelMapper = new ModelMapper();
        this.categoryItemService = categoryItemService;
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseAddProduct> saveProduct(@ModelAttribute AddProductDTO data, HttpServletRequest httpServletRequest){
        ResponseAddProduct response = new ResponseAddProduct();
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                response.buildMessage("Unauthorization");
                return ResponseEntity.status(400).body(response);
            }

            Distributor distributor = this.distributorService.getDistributorByUserId(userId);
            if (distributor == null){
                response.buildMessage("You are not Distributor");
                return ResponseEntity.status(400).body(response);
            }
            Product product = this.modelMapper.map(data, Product.class);
            product.setDistributorId(distributor.getId());

            Product newProduct = this.productService.saveProduct(product);
            this.discountService.saveDiscount(new Discount(newProduct.getId(), 0.0));
            this.warehouseService.saveData(new Warehouse(newProduct.getId(), 0));
            if (data.getCategories() != null){
                for (int i=0; i<data.getCategories().size(); i++){
                    this.categoryItemService.saveItem(new CategoryItem(newProduct.getId(), data.getCategories().get(i)));
                }
            }
            //this.hadoopService.createFolder(newProduct.getId(), "products");
            File dir = new File(Constants.storagePath + "/products/" + newProduct.getId());
            dir.mkdir();


            if (data.getImages() != null){
                for (int i = 0; i<data.getImages().size(); i++){
                    //this.hadoopService.saveMedia(data.getImages().get(i), Integer.toString(i), "products/" + newProduct.getId());
                    data.getImages().get(i).transferTo(new File(Constants.storagePath + "/products/" + newProduct.getId() + "/" + i + ".jpg"));
                }
            }

            response.setProduct(newProduct);
            response.setMessage("Saved Product");
            response.setSuccess(true);
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            System.out.println(exception);
            response.setMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationProducts> paginationProducts(@RequestParam(name = "num", required = false) Integer num, @RequestParam(name = "page", required = false) Integer page){
        try{
            int totalProducts = this.productService.getAllProducts().size();
            if (num == null){
                num = totalProducts;
            }

            if (page == null){
                page = 1;
            }

            int totalPages = totalProducts/num;
            if (totalProducts%num != 0){
                totalPages++;
            }

            List<Product> productsInfo = this.productService.paginationProducts(num, page);
            List<InfoProduct> products = new ArrayList<>();
            for (Product product : productsInfo) {
                InfoProduct item = new InfoProduct();
                item.setDiscount(this.discountService.getDiscountByProductId(product.getId()));
                item.setProduct(product);
                item.setWarehouse(this.warehouseService.getWarehouseByProduct(product.getId()));
                item.setNumMedia(this.hadoopService.countItemsCluster("products/" + product.getId()));
                List<CategoryItem> cateItems = this.categoryItemService.getCategoriesItemByProduct(product.getId());
                List<Category> categories = new ArrayList<>();
                for (CategoryItem cateItem : cateItems) {
                    categories.add(this.categoryService.getCategoryById(cateItem.getCategoryId()));
                }
                item.setCategories(categories);
                products.add(item);
            }

            return ResponseEntity.status(200).body(new ResponsePaginationProducts(products, totalPages, totalProducts));
        }
        catch(Exception exception){
            return ResponseEntity.status(200).body(null);
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImageProduct(@PathVariable("id") String id, @RequestParam(name = "index") int index){
        return ResponseEntity.status(200).contentType(MediaType.IMAGE_JPEG).body(this.hadoopService.getImage(Integer.toString(index), "products/" + id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfoProduct> getProductById(@PathVariable("id") String id){
        try{
            Product product = this.productService.getProductById(id);
            if (product == null){
                return ResponseEntity.status(400).body(null);
            }
            InfoProduct infoProduct = new InfoProduct();
            infoProduct.setDiscount(this.discountService.getDiscountByProductId(id));
            infoProduct.setWarehouse(this.warehouseService.getWarehouseByProduct(id));
            infoProduct.setNumMedia(this.hadoopService.countItemsCluster("products/" + id));
            infoProduct.setProduct(product);

            List<CategoryItem> items = this.categoryItemService.getCategoriesItemByProduct(id);
            List<Category> categories = new ArrayList<>();
            for (CategoryItem categoryItem : items) {
                Category category = this.categoryService.getCategoryById(categoryItem.getCategoryId());
                categories.add(category);
            }

            infoProduct.setCategories(categories);

            return ResponseEntity.status(200).body(infoProduct);
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/distributor")
    public ResponseEntity<PaginationDetailProduct> getProductsByDistributor(HttpServletRequest httpServletRequest, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "num", required = false) Integer num){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(null);
            }

            Distributor distributor = this.distributorService.getDistributorByUserId(userId);
            if (distributor == null){
                return ResponseEntity.status(404).body(null);
            }

            if (page == null){
                page = 1;
            }
            int totalProducts = this.productService.getAllProducts().size();
            if (num == null){
                num = totalProducts;
            }

            int totalPages = totalProducts/num;
            if (totalProducts%num !=0){
                totalPages++;
            }

            List<Product> products = this.productService.paginationProductsByDistributor(distributor.getId(), page, num);
            List<InfoProduct> items = new ArrayList<>();
            for (Product product : products) {
                InfoProduct item = new InfoProduct();
                item.setProduct(product);
                item.setDiscount(this.discountService.getDiscountByProductId(product.getId()));
                item.setWarehouse(this.warehouseService.getWarehouseByProduct(product.getId()));
                item.setNumMedia(this.hadoopService.countItemsCluster("products/" + product.getId()));
                List<CategoryItem> cateItems = this.categoryItemService.getCategoriesItemByProduct(product.getId());
                List<Category> categories = new ArrayList<>();
                for (CategoryItem cateItem : cateItems) {
                    categories.add(this.categoryService.getCategoryById(cateItem.getCategoryId()));
                }
                item.setCategories(categories);

                items.add(item);
            }
            return ResponseEntity.status(200).body(new PaginationDetailProduct(items, totalPages, totalProducts));
        }   
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<Response> editProduct(@RequestBody Product product, HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(new Response(false, "Unauthorization"));
            }
            Distributor distributor = this.distributorService.getDistributorByUserId(userId);
            Product checkProduct = this.productService.getProductById(product.getId());
            if (distributor == null || checkProduct == null || distributor.getId().equals(checkProduct.getDistributorId()) == false){
                return ResponseEntity.status(400).body(new Response(false, "Not Allowed"));
            }

            if (this.productService.editProduct(product) == null){
                return ResponseEntity.status(400).body(new Response(false, "Failed to update"));
            }
            return ResponseEntity.status(200).body(new Response(true, "Success"));

        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(new Response(false, "Internal Error Server"));
        }
    }
    
}
