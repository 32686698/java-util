package com.tian.test.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFileNIO {

    /**
     * 通过NIO复制文件
     * @param sourceFile
     * @param targetFilePath
     * @throws IOException
     */
    public static void copyFile(File sourceFile,String targetFilePath)
    throws IOException {
        FileInputStream fis = new FileInputStream(sourceFile);
        FileChannel fcIn = fis.getChannel();
        FileOutputStream fos = new FileOutputStream(targetFilePath);
        FileChannel fcOut = fos.getChannel();
        ByteBuffer bbf = ByteBuffer.allocate(1024);
        while (fcIn.read(bbf)!=-1){
            bbf.flip();
            while(bbf.hasRemaining()){
                fcOut.write(bbf);
            }
            bbf.clear();
        }
        fcOut.close();
        fos.close();
        fcIn.close();
        fis.close();

    }


    public static void combinFile(String targetFilePath,File... files)
    throws IOException{
        FileOutputStream fos = new FileOutputStream(targetFilePath);
        FileChannel fcOut = fos.getChannel();
        for(File file:files){
            FileInputStream fi = new FileInputStream(file);
            FileChannel fcIn = fi.getChannel();
            fcIn.transferTo(0, fcIn.size(), fcOut);
            fcIn.close();
            fi.close();
        }
        fcOut.close();
        fcOut.close();
    }
}
