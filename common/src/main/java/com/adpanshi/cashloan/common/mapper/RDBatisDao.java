package com.adpanshi.cashloan.common.mapper;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface RDBatisDao {
	
	
	String value() default "";

}