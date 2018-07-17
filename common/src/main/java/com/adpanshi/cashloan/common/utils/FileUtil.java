package com.adpanshi.cashloan.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by zsw on 2018/6/23 0023.
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static void copyFile(String src, String dest)
            throws IOException
    {
        File srcFile = new File(src);
        File destFile = new File(dest);
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        FileUtils.copyFile(srcFile, destFile);
    }

    public static void copyFile(File srcFile, String dest)
            throws IOException
    {
        File destFile = new File(dest);
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        FileUtils.copyFile(srcFile, destFile);
    }

    public static String getSuffixName(String fileName)
    {
        if (fileName != null) {
            int index = fileName.lastIndexOf(".");
            if (index > 0) {
                return fileName.substring(index).toLowerCase();
            }
        }
        return null;
    }

    public static String getSuffixNameNoPoint(String fileName)
    {
        if (fileName != null) {
            int index = fileName.lastIndexOf(".");
            if (index > 0) {
                return fileName.substring(index + 1).toLowerCase();
            }
        }
        return null;
    }

    public static String getFileNameFromUrl(String url)
    {
        if (url != null) {
            int index = url.lastIndexOf("/");
            int otherIndex = url.lastIndexOf("\\");
            if (index > 0) {
                if (otherIndex > 0)
                    index = index > otherIndex ? index : otherIndex;
            }
            else {
                index = otherIndex;
            }
            if (index > 0) {
                return url.substring(index + 1).toLowerCase();
            }
        }
        return null;
    }

    public static void fileDownLoad(String sourceFilePath, String targetPath, String fileName)
    {
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(sourceFilePath);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(5000);
            is = con.getInputStream();
            byte[] bs = new byte[1024];

            File sf = new File(targetPath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            os = new FileOutputStream(sf.getPath() + File.separator + fileName);
            int len;
            while ((len = is.read(bs)) != -1)
                os.write(bs, 0, len);
        }
        catch (MalformedURLException me) {
            LOGGER.error(me.getMessage(), me);
        } catch (IOException ie) {
            LOGGER.error(ie.getMessage(), ie);
        } finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void storeFile(byte[] fileData, String targetPath, String fileName)
    {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new ByteArrayInputStream(fileData);
            byte[] bs = new byte[1024];

            File sf = new File(targetPath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            os = new FileOutputStream(sf.getPath() + File.separator + fileName);
            int len;
            while ((len = is.read(bs)) != -1)
                os.write(bs, 0, len);
        }
        catch (MalformedURLException me) {
            LOGGER.error(me.getMessage(), me);
        } catch (IOException ie) {
            LOGGER.error(ie.getMessage(), ie);
        } finally {
            try {
                os.close();
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void remove(String filePath)
            throws Exception
    {
        File file = new File(filePath);
        if (file.exists())
            file.delete();
    }
}
