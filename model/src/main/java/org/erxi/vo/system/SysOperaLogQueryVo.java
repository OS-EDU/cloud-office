package org.erxi.vo.system;

import lombok.Data;

@Data
public class SysOperaLogQueryVo {

	private String title;
	private String operaName;

	private String createTimeBegin;
	private String createTimeEnd;

}

