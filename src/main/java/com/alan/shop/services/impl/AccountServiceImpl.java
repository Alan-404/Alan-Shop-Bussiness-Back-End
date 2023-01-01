package com.alan.shop.services.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alan.shop.models.Account;
import com.alan.shop.repositories.AccountRepository;
import com.alan.shop.services.AccountService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final Generator generator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.generator = new Generator();
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Account addAccount(Account account) {
        try {
            account.setId(generator.generateId(Constants.lengthId));
            account.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            account.setPassword(this.bCryptPasswordEncoder.encode(account.getPassword()));
            return this.accountRepository.save(account);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public Account getAccountByUser(String userId) {
        try {
            Optional<Account> account = this.accountRepository.getAccountByUserId(userId);
            if (account.isPresent() == false) {
                return null;
            }
            return account.get();
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public boolean checkPassword(Account account, String password) {
        try{
            
            return this.bCryptPasswordEncoder.matches(password, account.getPassword());
        }
        catch(Exception exception){
            return false;
        }
    }

    @Override
    public boolean changePassword(Account account, String newPassword){
        try{
            account.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.accountRepository.save(account);
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

}
