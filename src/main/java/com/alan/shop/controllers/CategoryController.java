package com.alan.shop.controllers;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.category.AddCategoryDTO;
import com.alan.shop.dtos.category.EditCategoryDTO;
import com.alan.shop.dtos.category.ResponseAddCategory;
import com.alan.shop.dtos.category.ResponseEditCategory;
import com.alan.shop.dtos.category.ResponsePaginationCategories;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.middleware.RoleFilter;
import com.alan.shop.models.Category;
import com.alan.shop.services.AccountService;
import com.alan.shop.services.CategoryService;
import com.alan.shop.services.HadoopService;
import com.alan.shop.services.RoleService;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final JwtFilter jwtFilter;
    private final RoleService roleService;
    private final RoleFilter roleFilter;
    private final ModelMapper modelMapper;
    private final HadoopService hadoopService;

    public CategoryController(CategoryService categoryService, AccountService accountService, RoleService roleService, HadoopService hadoopService){
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.roleService = roleService;
        this.hadoopService = hadoopService;
        this.jwtFilter = new JwtFilter();
        this.roleFilter = new RoleFilter(this.accountService, this.roleService);
        this.modelMapper = new ModelMapper();
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseAddCategory> saveCategory(@ModelAttribute AddCategoryDTO data, HttpServletRequest httpServletRequest){
        ResponseAddCategory response = new ResponseAddCategory();
        try{    
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                response.buildMessage("Unauthorization");
                return ResponseEntity.status(400).body(response);
            }
            System.out.println(userId);
            if (this.roleFilter.checkRoleAdmin(userId) == false){
                response.buildMessage("Access Denied");
                return ResponseEntity.status(403).body(response);
            }
            Category category = this.modelMapper.map(data, Category.class);
            category.setName(category.getName().trim());
            
            boolean checkName = this.categoryService.checkNameCategory(category.getName());
            if (checkName == true){
                response.buildMessage("Duplicated Name of Category");
                return ResponseEntity.status(400).body(response);
            }
            Category newCategory = this.categoryService.saveCategory(category);

            this.hadoopService.saveMedia(data.getFile(), newCategory.getId(), "categories");

            response.buildSuccess(true).buildMessage("Saved Category").buildCategory(newCategory);
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            System.out.println(exception);
            response.buildMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") String id){
        try{
            
            if (id == null){
                return ResponseEntity.status(400).body(null);
            }
            Category category = this.categoryService.getCategoryById(id);
            if (category == null){
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.status(200).body(category);
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<ResponseEditCategory> editCategory(@ModelAttribute EditCategoryDTO data, HttpServletRequest httpServletRequest){
        ResponseEditCategory response = new ResponseEditCategory();
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                response.buildMessage("Unauthorization");
                return ResponseEntity.status(400).body(response);
            }

            if (this.roleFilter.checkRoleAdmin(userId) == false){
                response.buildMessage("Access Denied");
                return ResponseEntity.status(403).body(response);
            }
            Category category = this.modelMapper.map(data, Category.class);
            Category checkCategory = this.categoryService.getCategoryById(category.getId());
            if (checkCategory == null){
                response.setMessage("Category Not Found");
                return ResponseEntity.status(404).body(response);
            }

            response.setSuccess(true);
            response.setMessage("Updated Category");
            category.setCreatedAt(checkCategory.getCreatedAt());

            if (data.getFile() != null){
                this.hadoopService.saveMedia(data.getFile(), checkCategory.getId(), "categories");
            }

            response.setCategory(this.categoryService.editCategory(category));
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            response.setMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImageCategory(@RequestParam(name = "id") String id){
        try{
            return ResponseEntity.status(200).contentType(MediaType.IMAGE_JPEG).body(this.hadoopService.getImage(id, "categories"));
        }
        catch(Exception exception){
            return ResponseEntity.status(500).contentType(MediaType.IMAGE_JPEG).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(false);
            }
            if (this.roleFilter.checkRoleAdmin(userId) == false){
                return ResponseEntity.status(403).body(false);
            }
            if (id == null){
                return ResponseEntity.status(400).body(false);
            }

            Category category = this.categoryService.getCategoryById(id);
            if (category == null){
                return ResponseEntity.status(400).body(false);
            }

            boolean deleteCategory = this.categoryService.deleteCategory(category);
            this.hadoopService.deleteImage(id, "categories");   
            return ResponseEntity.status(200).body(deleteCategory);
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(false);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationCategories> getCategories(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "num", required = false) Integer num){
        try{
            if (page == null){
                page = 1;
            }
            int totalCategories = this.categoryService.getAllCategories().size();
            if (num == null){
                num = totalCategories;
            }
            int totalPage = totalCategories/num;
            if (totalCategories % num != 0){
                totalPage++;
            }

            return ResponseEntity.status(200).body(new ResponsePaginationCategories(this.categoryService.paginationCategories(num, page), totalPage, totalCategories));
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }

}
