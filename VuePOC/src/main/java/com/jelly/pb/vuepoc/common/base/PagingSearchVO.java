package com.jelly.pb.vuepoc.common.base;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 *  * @Author : "SleepyJelly"
 */
@Getter
@Setter
@ToString
public class PagingSearchVO extends BaseVO implements Serializable{
	
	private static final long serialVersionUID = 1707472552956016405L;
	
	
	private int curPage=1;				// current page
	private int rowSizePerPage=10;		// page / record	default 10
	private int pageSize=10;			// page size
	private int totalRowCount;			// total row cnt
	
	
	// calculated value based on input
	private int firstRow ;				// first record num (row num)
	private int lastRow;				// last record num (row num)
	private int totalPageCount;			// total page cnt
	private int firstPage;				// first page num from page list 
	private int lastPage;				// last page num from page list 
	
	
	/** 검색Keyword */
	private String searchKeyword;			//검색어

	private String selectedId;             //id로 검색
	

	/** 검색조건 */
	private String searchCondition = "";    //Mapper 단에서 이걸 통해 분기


	/** 검색사용여부 */
	private String searchUseYn = "";
	
	public void setUpPagination() {   //보여줄 페이지 계산용 함수  페이징 전에 실행 필수

		totalPageCount = (totalRowCount-1)/rowSizePerPage+ 1;
		firstRow = (curPage - 1) * rowSizePerPage + 1;
		lastRow = firstRow + rowSizePerPage-1;

		if(lastRow >= totalRowCount) {
			lastRow = totalRowCount;
		}

		firstPage = (  (curPage-1) / pageSize) * pageSize + 1;

		lastPage = firstPage + pageSize-1;

		if(lastPage > totalPageCount) {
			lastPage = totalPageCount;
		}

	}
	
	
}
