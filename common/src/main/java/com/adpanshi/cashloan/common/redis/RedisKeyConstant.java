package com.adpanshi.cashloan.common.redis;


import com.adpanshi.cashloan.common.enums.ContentEnum;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class RedisKeyConstant
{
    public static String getKey(Business business, Project project, String key)
    {
        return business.getContent() + project.getContent() + key;
    }

    public static enum Project implements ContentEnum
    {
        STAFF("staff_", Integer.valueOf(0)),
        CAR_SELLER("car_seller_", Integer.valueOf(1)),
        WAP("wap_", Integer.valueOf(2)),
        CHEOK("cheok_", Integer.valueOf(3)),
        PERSONAL("personal_", Integer.valueOf(4)),
        FOREIGN("foreign_", Integer.valueOf(5)),
        COMMON("common_", Integer.valueOf(6));

        private String content;
        private Integer value;

        public boolean equalsValue(Integer value)
        {
            return (value != null) && (value.equals(getValue()));
        }
        private Project(String content, Integer value) { this.content = content;
            this.value = value;
        }

        public String getContent()
        {
            return this.content;
        }

        public Integer getValue()
        {
            return this.value;
        }

        public static Project valueOf(Integer value) {
            for (Project b : values()) {
                if (b.value.equals(value))
                    return b;
            }
            return null;
        }
    }

    public static enum Business
            implements ContentEnum
    {
        B2B("b2b_", Integer.valueOf(0)),
        B2C("b2c_", Integer.valueOf(1)),
        COMMON("common_", Integer.valueOf(2));

        private String content;
        private Integer value;

        private Business(String content, Integer value) { this.content = content;
            this.value = value;
        }
        public boolean equalsValue(Integer value)
        {
            return (value != null) && (value.equals(getValue()));
        }
        public String getContent()
        {
            return this.content;
        }

        public Integer getValue()
        {
            return this.value;
        }

        public static Business valueOf(Integer value) {
            for (Business b : values()) {
                if (b.value.equals(value))
                    return b;
            }
            return null;
        }
    }
}
