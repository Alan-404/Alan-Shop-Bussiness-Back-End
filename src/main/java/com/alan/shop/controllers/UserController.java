package com.alan.shop.controllers;



import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.alan.shop.dtos.register.RegisterDTO;
import com.alan.shop.dtos.register.ReponseRegisterDTO;
import com.alan.shop.dtos.user.EditAvatarDTO;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Account;
import com.alan.shop.models.User;
import com.alan.shop.services.AccountService;
import com.alan.shop.services.HadoopService;
import com.alan.shop.services.UserService;
import com.alan.shop.utils.Constants;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final HadoopService hadoopService;
    private final ModelMapper modelMapper;
    private final JwtFilter jwtFilter;

    public UserController(UserService userService, AccountService accountService, HadoopService hadoopService){
        this.userService = userService;
        this.accountService = accountService;
        this.hadoopService = hadoopService;
        this.modelMapper = new ModelMapper();
        this.jwtFilter = new JwtFilter();
    }

    @PostMapping("/register")
    public ResponseEntity<ReponseRegisterDTO> addUser(@ModelAttribute RegisterDTO registerDTO){
        ReponseRegisterDTO response = new ReponseRegisterDTO();
        
        try{
            User checkUser = this.userService.getUserByEmail(registerDTO.getEmail());
            if (checkUser != null){
                response.setMessage("Email has been taken");
                return ResponseEntity.status(400).body(response);
            }
            
            User user = this.modelMapper.map(registerDTO, User.class);
            Account account = this.modelMapper.map(registerDTO, Account.class);
            User newUser = this.userService.addUser(user);
            account.setUserId(newUser.getId());
            if (this.accountService.addAccount(account) == null){
                return ResponseEntity.status(400).body(response);
            }

            registerDTO.getFile().transferTo(new File(Constants.storagePath + "/users/" + newUser.getId() + ".jpg"));

            response.setSuccess(true);
            response.setUser(newUser);
            response.setMessage("Register User Successfully");
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            exception.printStackTrace();
            response.setMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }


    @GetMapping("/info/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String userId){
        try{
            if (userId == null){
                return ResponseEntity.status(400).body(null);
            }
            User user = this.userService.getUserById(userId);
 
            if (user == null){
                return ResponseEntity.status(404).body(null);
            }

            return ResponseEntity.status(200).body(user);
        }  
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/token")
    public ResponseEntity<User> getUserByToken(HttpServletRequest httpServletRequest){
        try{
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            if (authorizationHeader == null || authorizationHeader.startsWith("Bearer") == false){
                return ResponseEntity.status(400).body(null);
            }
            String token = authorizationHeader.split(" ")[1];
            if (token == null){
                return ResponseEntity.status(400).body(null);
            }
            String userId = this.jwtFilter.extractUserId(token);
            if (userId == null){
                return ResponseEntity.status(400).body(null);
            }

            User user = this.userService.getUserById(userId);
            if (user == null){
                return ResponseEntity.status(404).body(null);
            }

            return ResponseEntity.status(200).body(user);
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }

    }

    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getAvatarUser(@RequestParam String id){
        try{
            return ResponseEntity.status(200).contentType(MediaType.IMAGE_JPEG).body(Files.readAllBytes(Paths.get(Constants.storagePath + "/users/" + id + ".jpg")));
        }
        catch(Exception exception){
            return null;
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<Response> editProfileUser(@RequestBody User user, HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(new Response(false, "Unauthorization"));
            }
            User checkUser = this.userService.getUserById(userId);
            if (checkUser == null){
                return ResponseEntity.status(404).body(new Response(false, "Unknow User"));
            }
            if (this.userService.editUser(user) == null){
                return ResponseEntity.status(400).body(new Response(false, "Failed to Edit"));
            }
            return ResponseEntity.status(200).body(new Response(true, "Success"));
        }
        catch(Exception exception){
            exception.printStackTrace();
            return ResponseEntity.status(500).body(new Response(false, "Internal Error Server"));
        }
    }

    @PutMapping("/avatar")
    public ResponseEntity<Boolean> editAvatarUser(@RequestBody EditAvatarDTO data, HttpServletRequest httpServletRequest){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(false);
            }
            User user = this.userService.getUserById(userId);
            if (user == null){
                return ResponseEntity.status(404).body(false);
            }

            this.hadoopService.deleteImage(userId, "users");
            this.hadoopService.saveMedia(data.getFile(), userId, "users");

            return ResponseEntity.status(200).body(true);

        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(false);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers(){
        try{
            return ResponseEntity.status(200).body(this.userService.getUsers());
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }
}
