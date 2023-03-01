package org.erxi.model.system;

import org.erxi.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel(description = "SysOperaLog")
@TableName("sys_opera_log")
public class SysOperaLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "模块标题")
	@TableField("title")
	private String title;

	@ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除）")
	@TableField("business_type")
	private String businessType;

	@ApiModelProperty(value = "方法名称")
	@TableField("method")
	private String method;

	@ApiModelProperty(value = "请求方式")
	@TableField("request_method")
	private String requestMethod;

	@ApiModelProperty(value = "操作类别（0其它 1后台用户 2手机端用户）")
	@TableField("operator_type")
	private String operatorType;

	@ApiModelProperty(value = "操作人员")
	@TableField("opera_name")
	private String operaName;

	@ApiModelProperty(value = "部门名称")
	@TableField("dept_name")
	private String deptName;

	@ApiModelProperty(value = "请求URL")
	@TableField("opera_url")
	private String operaUrl;

	@ApiModelProperty(value = "主机地址")
	@TableField("opera_ip")
	private String operaIp;

	@ApiModelProperty(value = "请求参数")
	@TableField("opera_param")
	private String operaParam;

	@ApiModelProperty(value = "返回参数")
	@TableField("json_result")
	private String jsonResult;

	@ApiModelProperty(value = "操作状态（0正常 1异常）")
	@TableField("status")
	private Integer status;

	@ApiModelProperty(value = "错误消息")
	@TableField("error_msg")
	private String errorMsg;

	@ApiModelProperty(value = "操作时间")
	@TableField("opera_time")
	private Date operaTime;

}