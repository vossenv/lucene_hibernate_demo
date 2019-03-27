package com.dm.teamquery.data.service;


import com.dm.teamquery.data.repository.SearchInfoRepository;
import com.dm.teamquery.entity.SearchInfo;
import com.dm.teamquery.execption.customexception.SearchFailedException;
import com.dm.teamquery.data.SearchRequest;
import com.dm.teamquery.data.SearchResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public abstract class TeamQueryService<T, ID> {

    private String pClassName;
    private Class<T> persistentClass;
    private EntityManager em;
    private PagingAndSortingRepository<T, ID> repository;

    @Autowired
    private SearchInfoRepository infoRepository;

    public TeamQueryService(EntityManager em, PagingAndSortingRepository repository) {
        this.em = em;
        this.repository = repository;
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.pClassName = this.persistentClass.getSimpleName();
    }

    public T save(T t) {
        try {
            return findById(getEntityId(repository.save(t)));
        } catch (TransactionSystemException e) {
            Throwable th = ExceptionUtils.getRootCause(e);
            if (ExceptionUtils.getRootCause(e) instanceof ConstraintViolationException) {
                throw new ConstraintViolationException(((ConstraintViolationException) th).getConstraintViolations());
            } else {
                throw new PersistenceException(th.getMessage());
            }
        }
    }

    public void deleteById(ID id) {
        try {
            repository.deleteById(id);
            if (existsById(id)) throw new PersistenceException("Delete failed for unknown reasons!");
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(ExceptionUtils.getRootCauseMessage(e));
        } catch (InvalidDataAccessApiUsageException e) {
            throw new IllegalArgumentException(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public T findById(ID id) {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundException("No entity was found for id: " + id.toString());
        } catch (InvalidDataAccessApiUsageException e) {
            throw new IllegalArgumentException(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public List<T> findAll() {
        List<T> results = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(results::add);
        return results;
    }

    private ID getEntityId(T t) {
        return (ID) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(t);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }


    public List<T> search(String query, Pageable p) {
        return em
                .createQuery(query)
                .setMaxResults(p.getPageSize())
                .setFirstResult(p.getPageNumber() * p.getPageSize())
                .getResultList();
    }

    public long count(String query) {
        return (long) em.createQuery("select count(*) " + query).getResultList().get(0);
    }

    public List<T> basicSearch(String query) throws SearchFailedException {
        return (List<T>) search(new SearchRequest(query)).getResultsList();
    }

    public SearchResponse search(SearchRequest request) throws SearchFailedException {

        return null;

//        long startTime = System.nanoTime();
//        String query = request.getQuery();
//        String dbQuery = prepareQuery(new Search(persistentClass, query).getDatabaseQuery(), request.getIncDisabled());
//        SearchInfo search = new SearchInfo(query, dbQuery);
//        SearchResponse response = new SearchResponse(request);
//
//        try {
//            response.setRowCount(count(dbQuery));
//            response.setResultsList(search(dbQuery, request.getPageable()));
//            response.setSearchTime((System.nanoTime() - startTime) * 1.0e-9);
//            infoRepository.save(search);
//            return response;
//        } catch (Exception e) {
//            search.setErrors(ExceptionUtils.getRootCauseMessage(e));
//            infoRepository.save(search);
//            throw new SearchFailedException(ExceptionUtils.getRootCauseMessage(e));
//        }
    }

    private String prepareQuery(String query, boolean disabled) {
        String key = disabled ? "0" : "1";
        if (query.equals("from " + pClassName))
            return query + " where enabled = " + key;
        else {
            return query.replace("from " + pClassName + " where ",
                    "from " + pClassName + " where enabled = " + key + " and (") + ")";
        }
    }

}

