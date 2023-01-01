package com.alan.shop.services;

import java.util.List;

import com.alan.shop.models.Banner;

public interface BannerService {
    public Banner saveBanner(Banner banner);
    public boolean deleteBanner(String id);
    public Banner getBannerById(String id);
    public List<Banner> getAllBanners();
    public List<Banner> getBanners(int num, int page);
}
