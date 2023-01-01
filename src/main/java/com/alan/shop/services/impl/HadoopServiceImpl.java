package com.alan.shop.services.impl;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alan.shop.services.HadoopService;
import com.alan.shop.utils.Constants;

@Service
public class HadoopServiceImpl implements HadoopService {
    @Override
    public boolean saveMedia(MultipartFile file, String name, String clusterName){
        try{
            Configuration config = new Configuration();
            config.set(Constants.defaultFS, Constants.hdfsSite);
            FileSystem fs = FileSystem.get(config);
            Path hdfsPath = new Path(Constants.pathPrefix + clusterName + "/" + name + ".jpg");
            FSDataOutputStream outputStream = fs.create(hdfsPath);

            outputStream.write(file.getBytes());

            fs.close();
            return true;
        }
        catch(Error | IOException e){
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public byte[] getImage(String name, String clusterName){
        try{
            Configuration config = new Configuration();
            config.set(Constants.defaultFS, Constants.hdfsSite);
            FileSystem fs = FileSystem.get(config);

            Path hdfsPath = new Path(Constants.pathPrefix  + clusterName + "/" + name + ".jpg");


            FSDataInputStream inputStream = fs.open(hdfsPath);

            byte[] arrImage = IOUtils.readFullyToByteArray(inputStream);


            return arrImage;
        }
        catch(Error | IOException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean deleteImage(String name, String clusterName){
        try{
            Configuration config = new Configuration();
            config.set(Constants.defaultFS, Constants.hdfsSite);
            FileSystem fs = FileSystem.get(config);

            Path hdfsPath = new Path(Constants.pathPrefix  + clusterName + "/" + name + ".jpg");
            fs.delete(hdfsPath, true);
            fs.close();
            return true;
        }
        catch(Error | IOException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean createFolder(String name, String clusterName){
        try{
            Configuration config = new Configuration();
            config.set(Constants.defaultFS, Constants.hdfsSite);
            FileSystem fs = FileSystem.get(config);
            Path hdfsPath = new Path(Constants.pathPrefix + clusterName + "/" + name);
            fs.mkdirs(hdfsPath);

            fs.close();
            return true;
        }
        catch(Error | IOException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int countItemsCluster(String clusterName){
        try{
            Configuration config = new Configuration();
            config.set(Constants.defaultFS, Constants.hdfsSite);
            FileSystem fs = FileSystem.get(config);
            Path hdfsPath = new Path(Constants.pathPrefix + clusterName);
            RemoteIterator<LocatedFileStatus> files = fs.listFiles(hdfsPath, false);
            
            int count = 0;
            while(files.hasNext()){
                count++;
                files.next();
            }
            
            return count;
        }
        catch(Exception exception){
            exception.printStackTrace();
        }

        return 0;
    }

}
