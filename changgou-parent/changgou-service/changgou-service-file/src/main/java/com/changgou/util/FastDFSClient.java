package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * fastdfs文件上传，下载工具类
 */
public class FastDFSClient {

    static{
        String conf = new ClassPathResource("fdfs_client.conf").getPath();
        try {
            ClientGlobal.init(conf);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    public static String[] upload(FastDFSFile file){
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair(file.getAuthor());
        TrackerClient trackerClient = new TrackerClient();
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            String[] uploadFile = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
            return uploadFile;

        } catch (IOException | MyException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据组名和文件名获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFileInfo(String groupName,String remoteFileName){
        TrackerClient trackerClient = new TrackerClient();
        FileInfo fileInfo = null;
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            try {

                fileInfo =  storageClient.get_file_info(groupName,remoteFileName);
            } catch (MyException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    public static TrackerServer getTrackerServer(){
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;

        try {
            trackerServer = trackerClient.getConnection();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return trackerServer;

    }

    /**
     * 获取storageClient
     * @return
     */
    public static StorageClient getStorageClient(){
        StorageClient storageClient = null;

        storageClient = new StorageClient(getTrackerServer(),null);

        return storageClient;
    }

    /**
     * 文件下载
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream download(String groupName , String remoteFileName){
        InputStream inputStream = null;
        try {
            byte[] bytes = getStorageClient().download_file(groupName, remoteFileName);
            inputStream = new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     */
    public void deleteFile(String groupName , String remoteFileName){

        try {
            getStorageClient().delete_file(groupName,remoteFileName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取组信息
     * @return
     */
    public static StorageServer getStorageServer(String groupName){

        try {
            return new TrackerClient().getStoreStorage(getTrackerServer(),groupName);
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 获取服务器信息：ip，port
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static ServerInfo[] getServerInfo(String groupName , String remoteFileName){
        try {
            return new TrackerClient().getFetchStorages(getTrackerServer(),groupName,remoteFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static String getTrackerUrl(){

        return "http://" + getTrackerServer().getInetSocketAddress().getHostString()+":" + ClientGlobal.getG_tracker_http_port();
    }


}
