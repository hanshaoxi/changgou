package com.changgou.file;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class FastDFSClientTest {
    @Before
    public void initConf(){

        try {
            ClientGlobal.init("src/main/resources/fdfs_client.conf");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void upload(){

        try {
            ClientGlobal.init("src/main/resources/fdfs_client.conf");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer,null);
            String[] jpgs = storageClient.upload_file("D:\\BaiduNetdiskDownload\\doc\\doc\\hadoop-rpc架构.jpg","jpg",null);
            for(String jpg : jpgs){
                System.out.println(jpg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void download(){

        try {
            ClientGlobal.init("src/main/resources/fdfs_client.conf");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer,null);
            byte[] downloadedFile = storageClient.download_file("group1","M00/00/00/wKjThF9XjbOADujhAABk1TyWGio483.jpg");
            File  file = new File("d:\\1.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(downloadedFile);
            bufferedOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }



    }

    @Test
    public void getFileInfo(){

        try {
            ClientGlobal.init("src/main/resources/fdfs_client.conf");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            FileInfo group = storageClient.get_file_info("group1", "M00/00/00/wKjThF9XjbOADujhAABk1TyWGio483.jpg");
            System.out.println(group.getSourceIpAddr());
            System.out.println(group.getFileSize());
            System.out.println(group.getCreateTimestamp());
            System.out.println(group.getCrc32());
            System.out.println(group);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getGroupInfo(){
        TrackerClient trackerClient = new TrackerClient();
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer,"group1");
            System.out.println(storageServer.getStorePathIndex());
            ServerInfo[] groups = trackerClient.getFetchStorages(trackerServer, "group1", "M00/00/00/wKjThF9XjbOADujhAABk1TyWGio483.jpg");
            for(ServerInfo s : groups){
               System.out.println(s.getIpAddr());
               System.out.println(s.getPort());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void getTrackerInfo(){
        TrackerClient trackerClient = new TrackerClient();
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            InetSocketAddress inetSocketAddress = trackerServer.getInetSocketAddress();
            System.out.println(inetSocketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
