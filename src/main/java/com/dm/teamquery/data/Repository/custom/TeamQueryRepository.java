package com.dm.teamquery.data.repository.custom;

import com.dm.teamquery.execption.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface TeamQueryRepository<T, ID extends Serializable> {

    T findByIdThrows(ID id) throws TeamQueryException;

    List<T> findAll();

    List<T> search(String query, Pageable p);

    long count(String query);

}