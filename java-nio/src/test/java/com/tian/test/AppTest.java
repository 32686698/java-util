package com.tian.test;

import static org.junit.Assert.assertTrue;

import com.tian.test.io.CopyFile;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
    public void testCopyFileReadLine()  throws IOException {
        CopyFile.copyFileReadLine(new File("d:/settings.xml"), "e:/bb.xml");
    }
}
