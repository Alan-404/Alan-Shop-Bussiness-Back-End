package com.alan.shop.utils.queries;

public class BannerQuery {
    public static final String queryGetAllBanners = "SELECT * FROM BANNERS";
    public static final String queryGetAllBannersNewest = "SELECT * FROM BANNERS ORDER BY CREATED_AT ?1";
    public static final String queryGetAllBannersPage = "SELECT * FROM BANNERS WHERE STATUS=TRUE ORDER BY CREATED_AT DESC LIMIT ?1 OFFSET ?2";
}
