package com.jelly.pb.vuepoc.openai.service.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AudioSummaryHistoryVO {
	
	private long dmndSn;
	
	private	String prgrsSttsCd;
	private	String sumryDmndCn;
	private	String sumryRspnsCn;
	
	private	String frstRegrId;
	private	String frstRegDt;
	private	String lastChgrId;
	private	String lastChgDt;
	
	private	long fileSn;

}
