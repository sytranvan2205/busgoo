package com.iuh.busgoo.mapper;

import java.util.List;

public interface BaseMapper<E, D> {
	E toEntity(D dto);

	D toDto(E entity);

	List<E> toEntity(List<D> dtoList);

	List<D> toDto(List<E> entityList);
}
