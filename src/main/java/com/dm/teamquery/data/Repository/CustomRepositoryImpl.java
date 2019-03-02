package com.dm.teamquery.data.repository;

import com.dm.teamquery.execption.DeleteFailedException;
import com.dm.teamquery.execption.EntityLookupException;
import com.dm.teamquery.execption.EntityNotFoundException;
import com.dm.teamquery.execption.InvalidEntityIdException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.NoSuchElementException;

public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID> {

    private final EntityManager entityManager;

    public CustomRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public void refresh(T t) {
        entityManager.refresh(t);
    }

    @Override
    public <S extends T> S save(S entity) {
        try {
            refresh(super.save(entity));
        } catch (Exception e) {
            throw new PersistenceException(ExceptionUtils.getRootCauseMessage(e));
        }
        return entity;
    }

    @Override
    public T findEntityById(ID id) throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {
        try {
            return findById(id).get();
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundException("No entity was found for id: " + id.toString());
        } catch (IllegalArgumentException e) {
            throw new InvalidEntityIdException(ExceptionUtils.getRootCauseMessage(e));
        } catch (Exception e) {
            throw new EntityLookupException(e.getClass().getSimpleName()
                    + " - " + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    @Override
    @Transactional
    public void deleteEntityById(ID id) throws EntityNotFoundException, DeleteFailedException {
        try {
            deleteById(id);
        } catch (EmptyResultDataAccessException | IllegalArgumentException e) {
            throw new EntityNotFoundException(e.getStackTrace(),
                    "No entity was found for id: " + id, ExceptionUtils.getRootCauseMessage(e));
        } catch (Exception e) {
            throw new DeleteFailedException(
                    e.getClass().getSimpleName() + " - " + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    @Override
    public boolean existsEntity(ID id) {
        return findById(id).isPresent();
    }


//    @Override
//    public T update(T t) {
//        return null;
//    }
}