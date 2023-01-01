package com.alan.shop.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alan.shop.dtos.Response;
import com.alan.shop.dtos.banner.AddBannerDTO;
import com.alan.shop.dtos.banner.ResponsePaginationBanners;
import com.alan.shop.models.Banner;
import com.alan.shop.services.BannerService;
import com.alan.shop.services.HadoopService;


@RestController
@RequestMapping("/banner")
public class BannerController {
    private final BannerService bannerService;
    private final ModelMapper modelMapper;
    private final HadoopService hadoopService;

    public BannerController(BannerService bannerService, HadoopService hadoopService){
        this.bannerService = bannerService;
        this.modelMapper = new ModelMapper();
        this.hadoopService = hadoopService;
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addBanner(@ModelAttribute AddBannerDTO data){
        Response response = new Response();
        try{    
            Banner banner = this.modelMapper.map(data, Banner.class);
            Banner newBanner = this.bannerService.saveBanner(banner);

            this.hadoopService.saveMedia(data.getFile(), newBanner.getId(), "banners");

            response.buildMessage("Add Banner Successfully").buildSuccess(true);
            return ResponseEntity.status(200).body(response);

        }
        catch(Exception exception){
            response.buildMessage("Internal Error Server").buildSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delelteBanner(@PathVariable("id") String id){
        Response response = new Response();
        try{
            if (id == null){
                response.buildMessage("Invalid Banner");
                return ResponseEntity.status(400).body(response);
            }
            boolean deleteBanner = this.bannerService.deleteBanner(id);
            if (deleteBanner == false){
                response.buildMessage("Invalid Banner");
                return ResponseEntity.status(400).body(response);
            }
            this.hadoopService.deleteImage(id, "banners");
            response.buildMessage("Deleted Banner").buildSuccess(true);
            return ResponseEntity.status(200).body(response);
        }
        catch(Exception exception){
            response.buildMessage("Internal Error Server").buildSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationBanners> getBanners(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "num", required = false) Integer num){
        try{
            int totalProduct = this.bannerService.getAllBanners().size();
            if (page == null){
                page = 1;
            }
            if (num == null){
                num = totalProduct;
            }
            int totalPage = totalProduct / num;
            if (totalProduct%num != 0)
                totalPage++;
            return ResponseEntity.status(200).body(new ResponsePaginationBanners(this.bannerService.getBanners(num, page), totalPage));
        }
        catch(Exception exception){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImageBanner(@RequestParam String id){
        return ResponseEntity.status(200).contentType(MediaType.IMAGE_JPEG).body(this.hadoopService.getImage(id, "banners"));
    }
}
