package com.dm.teamquery.data.service;


import com.dm.teamquery.data.SearchRequest;
import com.dm.teamquery.data.SearchResponse;
import com.dm.teamquery.data.repository.SearchInfoRepository;
import com.dm.teamquery.entity.EntityBase;
import com.dm.teamquery.entity.SearchInfo;
import com.dm.teamquery.execption.customexception.SearchFailedException;
import com.dm.teamquery.search.SearchParameters;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.TransactionSystemException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public abstract class TeamQueryService<T extends EntityBase, ID> {

    private Class<T> persistentClass;
    private EntityManager em;
    private PagingAndSortingRepository<T, ID> repository;

    @Inject
    private SearchInfoRepository infoRepository;

    @Inject
    private SearchService searchService;

    public TeamQueryService(EntityManager em, PagingAndSortingRepository repository) {
        this.em = em;
        this.repository = repository;
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @PostConstruct
    void setClass() {
        this.searchService.setEntityType(persistentClass);
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

    public List<T> basicSearch(String query) throws SearchFailedException {
        SearchRequest s = new SearchRequest(query);
        s.setIncDisabled(true);
        return (List<T>) search(s).getResultsList();
    }

    private SearchResponse search(SearchRequest request) throws SearchFailedException {
        try {

            long startTime = System.nanoTime();
            SearchResponse response = new SearchResponse(request);
            SearchParameters sp = new SearchParameters.Builder()
                    .withQuery(request.getQuery())
                    .incDisabled(request.getIncDisabled())
                    .withPageable(request.getPageable())
                    .build();

            response.setRowCount(searchService.count(sp));
            response.setResultsList(searchService.search(sp));
            response.setSearchTime((System.nanoTime() - startTime) * 1.0e-9);
            return response;
        } catch (Exception e) {
            infoRepository.save(new SearchInfo(request.getQuery(), e.getMessage()));
            throw (e instanceof SearchFailedException) ? (SearchFailedException) e
                    : new SearchFailedException(ExceptionUtils.getRootCauseMessage(e));
        }
    }

}

