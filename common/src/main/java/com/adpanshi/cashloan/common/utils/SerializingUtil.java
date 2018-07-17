package com.adpanshi.cashloan.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class SerializingUtil
{
    public static byte[] serialize(Object source)
    {
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream objOut = null;
        try {
            byteOut = new ByteArrayOutputStream();
            objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(source);
            objOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != objOut)
                    objOut.close();
            }
            catch (IOException e) {
                objOut = null;
            }
        }
        return byteOut.toByteArray();
    }

    public static Object deserialize(byte[] source)
    {
        ObjectInputStream objIn = null;
        Object retVal = null;
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(source);
            objIn = new ObjectInputStream(byteIn);
            retVal = objIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != objIn)
                    objIn.close();
            }
            catch (IOException e) {
                objIn = null;
            }
        }
        return retVal;
    }
}
