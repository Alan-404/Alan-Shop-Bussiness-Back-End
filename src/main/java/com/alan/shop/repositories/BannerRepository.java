package com.alan.shop.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alan.shop.models.Banner;
import com.alan.shop.utils.queries.BannerQuery;

public interface BannerRepository extends JpaRepository<Banner, String> {
    @Query(value = BannerQuery.queryGetAllBannersPage, nativeQuery = true)
    Optional<List<Banner>> paginationBanners(int num, int page);
}
