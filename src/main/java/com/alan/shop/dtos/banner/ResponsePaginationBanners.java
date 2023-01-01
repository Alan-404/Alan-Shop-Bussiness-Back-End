package com.alan.shop.dtos.banner;

import java.util.List;

import com.alan.shop.models.Banner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePaginationBanners {
    private List<Banner> banners;
    private int totalPage;
}
