package com.adpanshi.cashloan.common.utils;
import com.google.common.collect.Lists;
import java.util.List;
import javax.mail.internet.InternetAddress;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class EmailUtil {private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

    public static void sendEmail(String hostName, String senderName, String senderPassWord, String title, String activityAddress, String subject, String content)
    {
        Email email = new SimpleEmail();
        email.setHostName(hostName);
        List toList = Lists.newArrayList();
        InternetAddress toAddress = new InternetAddress();

        toAddress.setAddress(activityAddress);
        toList.add(toAddress);
        try {
            email.setTo(toList);
            email.setFrom(senderName, title);
            email.setAuthentication(senderName, senderPassWord);
            email.setSubject(subject);
            email.setContent(content, "text/plain");
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
            LOGGER.debug("发邮件异常");
        }
    }
}
