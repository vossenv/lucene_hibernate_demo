package com.dm.teamquery.data.repository;

        import com.dm.teamquery.execption.DeleteFailedException;
        import com.dm.teamquery.execption.EntityLookupException;
        import com.dm.teamquery.execption.EntityNotFoundException;
        import com.dm.teamquery.execption.InvalidEntityIdException;
        import org.springframework.data.repository.NoRepositoryBean;
        import org.springframework.data.repository.PagingAndSortingRepository;

        import java.io.Serializable;

@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {


    boolean existsEntity(ID id);
    void refresh(T t);

    T findEntityById(ID id)  throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException;
    void deleteEntityById(ID id)  throws EntityNotFoundException, DeleteFailedException;


    //T update(T t);
    //T saveCustom(T t);
}