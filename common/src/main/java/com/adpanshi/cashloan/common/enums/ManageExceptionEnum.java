package com.adpanshi.cashloan.common.enums;

/**
 * 
 * @Description :manage异常枚举类
 * @author nmnl
 * @modefyAuther huangqin
 * @version 1.1
 * @Date 201701206
 * @modefyDate 20171215
 *
 */
public enum ManageExceptionEnum {
	/**1--------------------------------------------------起点-----------------------------------------------------*/

	/**2--------------------------------------------------系统级别-----------------------------------------------------*/
	SUCCEED_CODE_VALUE               				(2000,"接口调用成功"),
	FAIL_CODE_VALUE               					(2001,"接口调用失败"),
	/**4--------------------------------------------------接口各种异常状态（非系统级异常），以40开头-----------------------------------------------------*/
	FAIL_CODE_PARAM_INSUFFICIENT	 				(4000,"请求参数异常,请联系开发人员!"),
	FAIL_UPDATE_CODE_VALUE	 						(4001,"修改数据异常,请联系开发人员!"),
	FAIL_INSERT_CODE_VALUE	 						(4002,"保存数据异常,请联系开发人员!"),
	FAIL_DELETE_CODE_VALUE	 						(4003,"删除数据异常,请联系开发人员!"),
	/*ManageProfitController*/
	SYS_USER_NO_PHONE_CODE_VALUE					(4020,"用户手机号码为空！"),
	/*ManageSystemManagementController*/
	MANAGE_SYS_MANAGE_ROLE_EXIST_CODE_VALUE			(4100,"角色nid已存在，保存失败！"),
	MANAGE_SYS_MANAGE_ROLE_USED_CODE_VALUE			(4101,"角色有用户在使用，删除失败！"),
	MANAGE_SYS_MANAGE_REPEATED_NUMBER_CODE_VALUE	(4102,"工号已存在，不能重复注册！"),
	MANAGE_SYS_MANAGE_REPEATED_USERNAME_CODE_VALUE	(4103,"用户名已存在，不能重复注册！"),
	MANAGE_SYS_MANAGE_NO_SYSUSER_CODE_VALUE			(4104,"未找到用户！"),
	MANAGE_SYS_MANAGE_CASHE_REFRESH_CODE_VALUE		(4105,"后台缓存刷新完成，前台缓存刷新失败！"),
	MANAGE_SYS_MANAGE_DICTDETIAL_EXIST_CODE_VALUE	(4106,"存在字典字项，删除失败！"),
	MANAGE_SYS_MANAGE_CONFIG_CODE_EXIST_CODE_VALUE	(4107,"对应编号code已存在，新增失败！"),
	/*ManageChannelController,ManageCreditDataController,ManageExamineController,ManageRepayNoticeController*/
	MANAGE_CHANNEL_EXIST_CODE_VALUE					(4201,"新增失败，对应的渠道已经存在！"),
	MANAGE_TPPBUSS_EXIST_CODE_VALUE					(4202,"新增失败，对应的接口简称已经存在！"),
	MANAGE_SCENEBUSS_EXIST_CODE_VALUE				(4203,"新增失败，对应的场景接口关联关系已经存在！"),
	MANAGE_EXAMINE_EXIST_CODE_VALUE					(4204,"新增失败，对应的信审人员已经存在！"),
	MANAGE_EXAMINE_QUERY_STATE_ERROR_CODE_VALUE		(4205,"查询失败，当前状态不可查询！"),
	MANAGE_EXAMINE_VERIFY_ERROR_CODE_VALUE		    (4206,"复审失败，请联系管理员！"),
	MANAGE_EXAMINE_VERIFY_STATE_ERROR_CODE_VALUE	(4207,"人工复审失败,当前状态不是待人工复审！"),
	MANAGE_EXAMINE_BORROW_NOTEXIST_CODE_VALUE		(4208,"人工复审失败,当前标不存在！"),
	MANAGE_APPPUSH_ERROR_CODE_VALUE					(4209,"消息推送失败，请联系开发人员！"),
	MANAGE_APPPUSH_NO_DESCRIPTION_CODE_VALUE		(4210,"推送标题不能为空，请重新填写！"),
	MANAGE_APPPUSH_NO_CONTENT_CODE_VALUE			(4211,"推送内容不能为空，请重新填写！"),
	MANAGE_APPPUSH_FILE_TYPE_ERROR_CODE_VALUE		(4212,"请上传.xls或者.xlsx格式的符合模版格式的EXCEL文件！"),
	MANAGE_APPPUSH_CHECK_NO_DATA_CODE_VALUE			(4213,"未检测到上传数据，请确认后重新推送！"),
	//ManagePayController,ManageLoanOtherController
	MANAGE_PAY_TO_EXAMINE							(4301,"审核失败,请联系开发人员!"),
	MANAGE_LOAN_OTHER_NOT_BORROW					(4302,"查无此订单,请联系开发人员!"),
	/*ManageCollectionSelfOrderController,ManageCollectionWarningController*/
	MANAGE_COLL_PUSHDATA_ERROR_CODE_VALUE			(4401,"新增反馈信息提交信息有误!"),
	MANAGE_COLL_BORROW_NOTEXIST_CODE_VALUE			(4402,"借款信息不存在!"),
	MANAGE_COLL_BORROW_NOTOVERDUE_CODE_VALUE		(4403,"借款信息未逾期!"),
	MANAGE_COLL_BORROW_COLLEXIST_CODE_VALUE			(4404,"已存在催收订单中，请勿重复添加!"),
	/*Rule*Controller 规则引擎*/
	RULE_INFO_PARAM_ERROR_CODE_VALUE				(4501,"添加失败，参数错误!"),
	/*QuartzInfoController 定时任务*/
	QUART_NO_QUART_CODE_VALUE						(4601,"定时任务不存在，请联系开发人员!"),
	QUART_NO_QUART_STATE_CODE_VALUE					(4602,"定时任务状态异常，请联系开发人员!"),
	QUART_NO_QUART_OR_DISABLED_STATE_CODE_VALUE		(4603,"定时任务不存在或已经停止，请联系开发人员!"),
	QUART_QUART_EXIST_CODE_VALUE					(4604,"定时任务已存在，不可重复添加!"),
	/*ManageBorrowRepayController 还款管理*/
	MANAGE_REPAY_NO_REPAYBORROW_CODE_VALUE			(4701,"还款计划不存在，请确认后还款!"),
	MANAGE_REPAY_BORROW_REPAYED_CODE_VALUE			(4702,"还款计划已还款，请确认后还款!"),
	MANAGE_REPAY_TIME_OVERDUE_CODE_VALUE			(4703,"还款时间已逾期，请确认后还款!"),
	MANAGE_REPAY_REPAY_AMOUNT_ERROR_CODE_VALUE		(4704,"还款金额不匹配，请确认后还款!"),
	MANAGE_REPAY_NOT_OVERDUE_CODE_VALUE				(4705,"还款时间还未逾期，请确认后还款!"),
	MANAGE_REPAY_INTREST_ERROR_CODE_VALUE			(4706,"逾期利息不匹配应还利息，请确认后还款!"),
	MANAGE_REPAY_MORE_REPAY_AMOUNT_CODE_VALUE		(4707,"还款金额不能大于应还金额，请确认后还款!"),
	MANAGE_REPAY_REMISSION_AMOUNT_ERROR_CODE_VALUE	(4708,"实际还款+减免金额与应还金额不匹配，请确认后还款!"),
	MANAGE_REPAY_UPDATE_REPAY_ERROR_CODE_VALUE		(4709,"更新还款信息出错，请确认后还款!"),
	MANAGE_REPAY_UPDATE_PROGRESS_ERROR_CODE_VALUE	(4710,"更新借款表和借款进度状态出错，请确认后还款!"),
	MANAGE_REPAY_NO_CREDIT_CODE_VALUE				(4711,"用户信用额度信息不存在，请确认后还款!"),
	MANAGE_REPAY_FILE_TYPE_ERROR_CODE_VALUE			(4712,"支持.xls和.xlsx格式文档，请上传格式正确的文档!"),
	MANAGE_REPAY_FAIL_FILE_TYPE_CODE_VALUE			(4713,"文档类型错误，请上传格式正确的文!"),
	MANAGE_REPAY_NO_REPAYBORROWLOG_CODE_VALUE		(4714,"还款记录不存在，请确认后还款！"),
	/*ManageZhiRongController 直融*/
	MANAGE_ZHIRONG_RETRY_ERROR						(4800,"重试失败，请联系系统开发人员！"),
	/**5--------------------------------------------------其他错误情况,5000-----------------------------------------------------*/
	OTHER_CODE_VALUE				 				(5000,"系统异常"),
    /**6--------------------------------------------------用户,以6开头-----------------------------------------------------*/
	NO_PERMISSION_CODE_VALUE         				(6000,"您没有访问权限,请联系管理员!"),
	NO_PERMISSION_OTHER_CODE_VALUE         			(6001,"系统异常,请联系管理员!"),
	LOGIN_ERROR_PASSWORD_ERROR_CODE_VALUE			(6501,"密码错误，请核对后输入"),
	LOGIN_ERROR_USER_LOCKED_CODE_VALUE			 	(6502,"该用户已被停用，请联系管理员！"),
	LOGIN_ERROR_CODE_VALUE			 				(6503,"登录失败,请联系管理员"),
	LOGIN_ERROR_NO_COUNT_CODE_VALUE			 		(6504,"未找到账号,请核对后输入"),
	LOGIN_ERROR_NO_ROLE_CODE_VALUE			 		(6505,"未找到该账号对应的角色"),
	LOGIN_CONFIRM_RELOGIN_CODE_VALUE			 	(6520,"该用户已经登录，是否确认登录？"),
	MODIFY_PASSWORD_OLD_ERROR_CODE_VALUE			(6601,"原密码输入不正确！"),
	MODIFY_PASSWORD_COMPARE_DIFFERENT_CODE_VALUE	(6602,"两个新密码不一致！"),
	MODIFY_PASSWORD_CANT_SAME_CODE_VALUE			(6603,"新密码不能和旧密码相同！"),
	MODIFY_PASSWORD_SYSTEM_LENGTH					(6604,"管理员帐号密码必须在12-24位之间！"),
	/**8--------------------------------------------------Cookie相关 85开头-----------------------------------------------------*/
	//Cookie失效，需要跳转重新登录
	NOCOOKIE_CODE_VALUE              				(8500,"您未登录或Cookie已失效,请登录后再操作"),
	/**#--------------------------------------------------业务相关 其他情况待分配-----------------------------------------------------*/

	PARAMETER_CHECKING_CODE			 				(7000,"参数校验不通过 "),
	CLIENT_EXCEPTION_CODE_VALUE		 				(9980,"连接异常（除请求超时）"),
	TIMEOUT_CODE_VALUE		 		 				(9990,"请求超时"),

	
	;
	
	/**
	 * 错误码
	 */
	private Integer code;
	
	/**
	 * 错误信息
	 */
	private String msg;
	
	ManageExceptionEnum(Integer code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public Integer Code(){
		return code;
	}
	
	public String Msg(){
		return msg;
	}
	
	/** @20170213  根据errorCode 获取enum对象  **/
	public static ManageExceptionEnum getByCode(Integer code){
		ManageExceptionEnum[] enums = values();
		for(ManageExceptionEnum e:enums){
			if(e.code.equals(code)){
				return e;
			}
		}
 		return null;
	}
	
}

