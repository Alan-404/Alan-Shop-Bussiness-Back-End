package com.alan.shop.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alan.shop.models.Banner;
import com.alan.shop.repositories.BannerRepository;
import com.alan.shop.services.BannerService;
import com.alan.shop.utils.Constants;
import com.alan.shop.utils.Generator;

@Service
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    private final Generator generator;

    public BannerServiceImpl(BannerRepository bannerRepository){
        this.bannerRepository = bannerRepository;
        this.generator = new Generator();
    }

    @Override
    public Banner saveBanner(Banner banner){
        banner.setId(this.generator.generateId(Constants.lengthId));
        banner.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        banner.setStatus(true);
        return this.bannerRepository.save(banner);
    }
    @Override
    public Banner getBannerById(String id){
        Optional<Banner> banner = this.bannerRepository.findById(id);
        if (banner.isPresent() == false){
            return null;
        }

        return banner.get();
    }

    @Override
    public boolean deleteBanner(String id){
        Banner banner = this.getBannerById(id);
        if (banner == null)
            return false;
        this.bannerRepository.delete(banner);
        return true;
    }

    @Override
    public List<Banner> getAllBanners(){
        return this.bannerRepository.findAll();
    }

    @Override
    public List<Banner> getBanners(int num, int page){
        Optional<List<Banner>> banners = this.bannerRepository.paginationBanners(num, (page - 1)*num);

        return banners.get();
    }
}
