package com.alan.shop.services;

import org.springframework.web.multipart.MultipartFile;

public interface HadoopService {
    public boolean saveMedia(MultipartFile file, String name, String clusterName);
    public byte[] getImage(String name, String clusterName);
    public boolean deleteImage(String name, String clusterName);
    public boolean createFolder(String name, String clusterName);
    public int countItemsCluster(String clusterName);
}
