package com.zhanghao.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 监控文件变化
 *
 * @author YL
 **/
public class FileWatcher {
 
    public static void main(String[] args) throws IOException, InterruptedException {
        // 监控的目录
        String filePath = "H:\\home\\zhanghao\\logs\\ruoyi\\logs";
        // 要监控的文件
      //  String fileName = "sys-error.log";
 
        // 这里监听
        new Thread(() -> {
            try {
                FileWatcher.watcherLog(filePath , str -> System.out.println((System.currentTimeMillis() / 1000) + ",监听到: " + str));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
 
        // // 这里写
        // new Thread(() -> {
        //     try {
        //         int i = 0;
        //         System.out.println("开始写入数据...");
        //         while (true) {
        //             FileWriter writer = new FileWriter(filePath+"\\" + fileName, true);
        //             Thread.sleep(1000);
        //             System.out.println("写入数据"+ i);
        //             writer.append(String.valueOf(i)).append("\n");
        //             //writer.write(String.valueOf(i));
        //             //writer.write("\n");
        //             writer.flush();
        //             // 刷完关闭在下次循环重新打开。这样效果好
        //             // 否则系统可能会批量刷盘，上面的监听效果就是 一批一批一，分批过来的
        //             writer.close();
        //             i ++;
        //         }
        //     } catch (IOException | InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }).start();
    }
 
    /**
     * 文件监控
     * 同步调用会阻塞
     *
     * @param filePath
     * @param consumer
     * @throws IOException
     * @throws InterruptedException
     */
    public static void watcherLog(String filePath,  Consumer<String> consumer) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
 
        Paths.get(filePath).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        // 文件读取行数
        AtomicLong lastPointer = new AtomicLong(0L);
        do {
            WatchKey key = watchService.take();
            List<WatchEvent<?>> watchEvents = key.pollEvents();
            watchEvents.stream().filter(
                    i -> StandardWatchEventKinds.ENTRY_MODIFY == i.kind()
                            // && fileName.equals(((Path) i.context()).getFileName().toString())
            ).forEach(i -> {
                if (i.count() > 1) {
                    // "重复事件"
                    return;
                }
 
                File configFile = Paths.get(filePath + "/" + i.context()).toFile();
                StringBuilder str = new StringBuilder();
                // 读取文件
                lastPointer.set(getFileContent(configFile, lastPointer.get(), str));
                System.out.println("文件名：" + ((Path)i.context()).getFileName().toString());
                if (str.length() != 0) {
                    consumer.accept(str.toString());
                }
            });
            key.reset();
        } while (true);
    }
 
    /**
     * beginPointer > configFile 时会从头读取
     *
     * @param configFile
     * @param beginPointer
     * @param str          内容会拼接进去
     * @return 读到了多少字节, -1 读取失败
     */
    private static long getFileContent(File configFile, long beginPointer, StringBuilder str) {
        if (beginPointer < 0) {
            beginPointer = 0;
        }
        RandomAccessFile file = null;
        boolean top = true;
        try {
            file = new RandomAccessFile(configFile, "r");
            if (beginPointer > file.length()) {
                return 0;
            }
            file.seek(beginPointer);
            String line;
            while ((line = file.readLine()) != null) {
                if (top) {
                    top = false;
                } else {
                    str.append("\n");
                }
                str.append(new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            }
            return file.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
 
 