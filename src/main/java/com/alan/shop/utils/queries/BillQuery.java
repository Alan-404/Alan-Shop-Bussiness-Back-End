package com.alan.shop.utils.queries;

public class BillQuery {
    public static final String queryGetBillsByUser = "SELECT * FROM BILLS WHERE USER_ID = ?1";
    public static final String paginateBillsByUser = "SELECT * FROM BILLS WHERE USER_ID = ?1 LIMIT ?2 OFFSET ?3";
}
