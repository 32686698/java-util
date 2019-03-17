package com.tian.test;

import static org.junit.Assert.assertTrue;

import com.tian.test.io.CopyFile;
import com.tian.test.nio.CopyFileNIO;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testCopyStream() throws IOException {
        CopyFile.copyFileStream(new File("d:/DGSetup_1422B1.exe"), "e:/aa.exe");
        CopyFile.copyFileStream(new File("d:/settings.xml"), "e:/bb.xml");
    }

    @Test
    public void testCopyReader() throws IOException {
//        CopyFile.copyFileReader(new File("d:/DGSetup_1422B1.exe"), "e:/aa.exe");
        CopyFile.copyFileReader(new File("d:/settings.xml"), "e:/bb.xml");
    }

    @Test
    public void testCopyFileReadLine() throws IOException {
        CopyFile.copyFileReadLine(new File("d:/settings.xml"), "e:/bb.xml");
    }

    @Test
    public void testCopyNIO() throws IOException {
        CopyFileNIO.copyFile(new File("d:/DGSetup_1422B1.exe"), "e:/aa.exe");
//        CopyFileNIO.copyFile(new File("d:/settings.xml"), "e:/bb.xml");
    }

    @Test
    public void testCombinFile() throws IOException {
        CopyFileNIO.combinFile("e:/abcd.txt", new File("d:/a.txt"), new File("d:/b.txt"), new File("d:/c.txt"), new File("d:/d.txt"));
    }

    public void tst(){
//        ServerSocketChannel
    }
}
