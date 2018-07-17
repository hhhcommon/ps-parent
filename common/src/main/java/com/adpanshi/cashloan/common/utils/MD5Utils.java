package com.adpanshi.cashloan.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by zsw on 2018/6/23 0023.
 */
public class MD5Utils
{
    public static String ecodeByMD5(String originstr)
    {
        String result = null;

        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        if (originstr != null)
        {
            try
            {
                MessageDigest md = MessageDigest.getInstance("MD5");

                byte[] source = originstr.getBytes("utf-8");

                md.update(source);

                byte[] tmp = md.digest();

                char[] str = new char[32];

                int i = 0; for (int j = 0; i < 16; i++)
            {
                byte b = tmp[i];

                str[(j++)] = hexDigits[(b >>> 4 & 0xF)];

                str[(j++)] = hexDigits[(b & 0xF)];
            }

                result = new String(str);
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String getMD5String(String convertStr)
    {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }
        try
        {
            byte[] bytes = digest.digest(convertStr.toString().getBytes("UTF-8"));
            return String.format("%032x", new Object[] { new BigInteger(1, bytes) });
        } catch (UnsupportedEncodingException e) {
        }
        throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
    }
}
