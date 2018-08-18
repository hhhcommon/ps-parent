package com.adpanshi.cashloan.common.enums;
/***
 ** @category 请用一句话来描述其用途...
 ** @author qing.yunhui
 ** @email: qingyunhui@smoney.cc
 ** @createTime: 2017年11月27日上午11:56:27
 **/
public enum OrderPrefixEnum implements ICommonEnum{

	/**付款前缀J-*/
	PAY("J","付款"),
	
	/**还款前缀-K*/
	REPAYMENT("K","还款"),
	
	/**退还前缀-L*/
	GIVE_BACK("L","退还");
	
    private final String code;
    
    private final String name;
    
    private OrderPrefixEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getKey() {
		return null;
	}
}
