package com.adpanshi.cashloan.common.enums;
/***
 ** @category 一般键值型枚举的接口
 ** @author qing.yunhui
 ** @email: qingyunhui@smoney.cc
 ** @createTime: 2017年8月18日下午6:22:16
 **/
public interface ICommonEnum {
	
	/**code(key) */
	public String getCode();
	
	/**name(value) */
	public String getName();
	
	/**当code值为数字类型时，在调用getKey时会将字符类型转换成Integer类型方便调用*/
	public Integer getKey();

}
