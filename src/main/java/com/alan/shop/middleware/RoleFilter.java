package com.alan.shop.middleware;

import com.alan.shop.models.Account;
import com.alan.shop.services.AccountService;
import com.alan.shop.services.RoleService;
import com.alan.shop.utils.Enums;

public class RoleFilter {
    private final AccountService accountService;
    private final RoleService roleService;

    public RoleFilter(AccountService accountService, RoleService roleService) {
        this.roleService = roleService;
        this.accountService = accountService;
    }

    public boolean checkRoleAdmin(String userId) {
        try {
            Account account = this.accountService.getAccountByUser(userId);
            if (this.roleService.getRoleById(account.getRole()).getName().equals(Enums.Role.ADMIN.name())) {
                return true;
            }
            return false;
        } catch (Exception exception) {
            return false;
        }
    }

}
