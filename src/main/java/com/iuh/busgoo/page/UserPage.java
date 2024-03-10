package com.iuh.busgoo.page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPage implements Pageable{

	private Integer itemPerPage;
	
	private Integer page;
	
	private Sort sort;
	
	@Override
	public int getPageNumber() {
		return page;
	}

	@Override
	public int getPageSize() {
		return itemPerPage;
	}

	@Override
	public long getOffset() {
		return page*itemPerPage;
	}

	@Override
	public Sort getSort() {
		return sort;
	}

	@Override
	public Pageable next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable previousOrFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable first() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable withPage(int pageNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

}
