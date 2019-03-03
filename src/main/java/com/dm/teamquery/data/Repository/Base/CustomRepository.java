package com.dm.teamquery.data.repository.Base;

import com.dm.teamquery.execption.DeleteFailedException;
import com.dm.teamquery.execption.EntityLookupException;
import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.InvalidEntityIdException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

    boolean existsEntity(T t);
    boolean existsEntity(ID id);

    T findEntityById(ID id) throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException;
    void deleteEntityById(ID id) throws EntityNotFoundException, DeleteFailedException;

    <S extends T> S saveEntity(S entity);
    List<T> findAllEntities();

   List<T> search(String query, Pageable p);
   long count (String query);


}