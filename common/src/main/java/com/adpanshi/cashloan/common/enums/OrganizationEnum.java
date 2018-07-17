package com.adpanshi.cashloan.common.enums;

/**
 * 组织机构类型
 * Created by zsw on 2018/7/15 0015.
 */
public class OrganizationEnum {

    /**
     * 国家
     */
    public enum CountryType implements ContentEnum {

        INDIA("印度", 91),
        ;

        private String content;
        private Integer value;

        private CountryType(String content, Integer value) {
            this.content = content;
            this.value = value;
        }
        public boolean equalsValue(Integer value)
        {
            return (value != null) && (value.equals(getValue()));
        }

        public static CountryType valueOf(Integer value) {
            CountryType[] entities = CountryType.values();
            for (CountryType entity : entities) {
                if (entity.getValue().equals(value)) {
                    return entity;
                }
            }
            return null;
        }
        public String getContent() {
            return this.content;
        }
        public Integer getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    /**
     * 产品类型
     */
    public static enum ProductType implements ContentEnum {

        OLOAN("oloan", 91001),
        ;

        private String content;
        private Integer value;

        private ProductType(String content, Integer value) {
            this.content = content;
            this.value = value;
        }
        public boolean equalsValue(Integer value)
        {
            return (value != null) && (value.equals(getValue()));
        }

        public static ProductType valueOf(Integer value) {
            ProductType[] entities = ProductType.values();
            for (ProductType entity : entities) {
                if (entity.getValue().equals(value)) {
                    return entity;
                }
            }
            return null;
        }

        public String getContent() {
            return this.content;
        }

        public Integer getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
