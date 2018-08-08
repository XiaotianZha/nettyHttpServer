package com.zhang.http.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.URL;

public class FileUtil {

    public static RandomAccessFile getReadAccessFile(String resource) throws FileNotFoundException {

        URL url=getClassLoader().getResource(resource);
        String path =url.toString();
        path=!path.contains("file:")?path:path.substring(5);
        return new RandomAccessFile(new File(path),"r");
    }

    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
}
