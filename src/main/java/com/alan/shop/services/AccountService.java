package com.alan.shop.services;

import com.alan.shop.models.Account;

public interface AccountService {
    public Account addAccount(Account acocunt);
    public Account getAccountByUser(String userId);
    public boolean checkPassword(Account account, String password);
    public boolean changePassword(Account account, String newPassword);
}   
