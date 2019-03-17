package com.tian.test.io;

import java.io.*;

public class CopyFile {

    /**
     * 通过输入输出流复制文件。
     * 可以复制二进制文件和文本文件
     * @param sourceFile
     * @param targetFilePath
     * @throws IOException
     */
    public static void copyFileStream(File sourceFile,String targetFilePath)
    throws IOException{
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(sourceFile);
            os = new FileOutputStream(targetFilePath);
            byte[] bs = new byte[1024];
            while (is.read(bs)!=-1){
                os.write(bs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            is.close();
            os.close();
        }
    }

    /**
     * 只能自制文本文件
     * @param sourceFile
     * @param targetFilePath
     * @throws IOException
     */
    public static void copyFileReader(File sourceFile,String targetFilePath)
    throws IOException{
        Reader reader = null;
        Writer writer = null;
        try {
            reader = new FileReader(sourceFile);
            writer = new FileWriter(targetFilePath);
            char[] chars = new char[1024];
            while (reader.read(chars)!=-1){
                System.out.print(chars);
                writer.write(chars);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            reader.close();
            writer.close();
        }
    }

    public static void copyFileReadLine(File sourceFile,String targetFilePath)
            throws IOException{
        Reader reader = null;
        BufferedReader br = null;
        Writer writer = null;
        BufferedWriter bw = null;
        try {
            reader = new FileReader(sourceFile);
            br = new BufferedReader(reader);
            writer = new FileWriter(targetFilePath);
            bw = new BufferedWriter(writer);
            String line = "";
            while ((line = br.readLine())!=null){
                bw.write(line+"\t\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw.close();
            writer.close();
            br.close();
            reader.close();
        }
    }
}
