package com.alan.shop.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alan.shop.services.AIService;
import com.alan.shop.utils.Constants;
import com.nimbusds.jose.shaded.json.JSONObject;


@Service
public class AIServiceImpl implements AIService {
    RestTemplate restTemplate;

    public AIServiceImpl(){
        this.restTemplate = new RestTemplate();
    }
    @Override
    public int checkStrengthPassword(String password){
        try{
            JSONObject data = new JSONObject();
            data.put("password", password);
            return this.restTemplate.postForObject(Constants.aiApi + "/password/check", data, Integer.class);
        }
        catch(Exception exception){
            return 0;
        }
    }
}
