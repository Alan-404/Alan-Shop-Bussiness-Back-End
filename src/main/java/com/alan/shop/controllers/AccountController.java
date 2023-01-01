package com.alan.shop.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.Response;
import com.alan.shop.dtos.account.ChangePasswordDTO;
import com.alan.shop.dtos.account.CheckPasswordStrengthDTO;
import com.alan.shop.dtos.account.ForgotPasswordDTO;
import com.alan.shop.dtos.login.LoginDTO;
import com.alan.shop.dtos.login.ResponseLoginDTO;
import com.alan.shop.middleware.JwtFilter;
import com.alan.shop.models.Account;
import com.alan.shop.models.User;
import com.alan.shop.services.AIService;
import com.alan.shop.services.AccountService;
import com.alan.shop.services.UserService;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;
    private final JwtFilter jwtFilter;
    private final AIService aiService;

    public AccountController(UserService userService, AccountService accountService, AIService aiService){
        this.userService = userService;
        this.accountService = accountService;
        this.jwtFilter = new JwtFilter();
        this.aiService = aiService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@RequestBody LoginDTO loginDTO){
        ResponseLoginDTO response = new ResponseLoginDTO();
        try{
            User user = this.userService.getUserByEmail(loginDTO.getEmail());
            if (user == null){
                response.setMessage("Incorrect User or Password");
                return ResponseEntity.status(400).body(response);
            }
            Account account = this.accountService.getAccountByUser(user.getId());
            if (this.accountService.checkPassword(account, loginDTO.getPassword()) == false){
                response.setMessage("Incorrect User or Password");
                return ResponseEntity.status(400).body(response);
            }
            String accessToken = this.jwtFilter.generateToken(user.getId());
            response.setSuccess(true);
            response.setMessage("Login Successfully");
            response.setAccessToken(accessToken);
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            response.setMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
    }
    @PutMapping("/forgot")
    public ResponseEntity<Response> forgotPassword(@RequestBody ForgotPasswordDTO data){
        System.out.println("ok");
        try{
            
            Account account = this.accountService.getAccountByUser(data.getUserId());
            if (account == null){
                return ResponseEntity.status(404).body(new Response(false, "Unknow User"));
            }

            if (this.accountService.changePassword(account, data.getNewPassword()) == false){
                return ResponseEntity.status(400).body(new Response(false, "Failed"));
            }
            return ResponseEntity.status(200).body(new Response(true, "Success"));
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/password")
    public ResponseEntity<Response> changePassword(HttpServletRequest httpServletRequest, @RequestBody ChangePasswordDTO data){
        try{
            String userId = this.jwtFilter.authorize(httpServletRequest);
            if (userId == null){
                return ResponseEntity.status(400).body(new Response(false, "Unauthorization"));
            }

            Account account = this.accountService.getAccountByUser(userId);
            if (account == null){
                return ResponseEntity.status(404).body(new Response(false, "Unknow User"));
            }

            if (this.accountService.checkPassword(account, data.getCurrentPassword()) == false){
                return ResponseEntity.status(400).body(new Response(false, "Incorrect Current Password"));
            }

            if (this.accountService.changePassword(account, data.getNewPassword()) == false){
                return ResponseEntity.status(400).body(new Response(false, "Incorrect Current Password"));
            }
            return ResponseEntity.status(200).body(new Response(true, "Success"));
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(new Response(false, "Internal Error Server"));
        }
    }

    @PostMapping("/password/strength")
    public ResponseEntity<Integer> checkStrenthPassword(@RequestBody CheckPasswordStrengthDTO data){
        try{
            return ResponseEntity.status(200).body(this.aiService.checkStrengthPassword(data.getPassword()));
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }

    
}
