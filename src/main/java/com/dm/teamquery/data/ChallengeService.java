package com.dm.teamquery.data;


import com.dm.teamquery.data.repository.ChallengeRepository;
import com.dm.teamquery.data.repository.SearchInfoRepository;
import com.dm.teamquery.entity.SearchInfo;
import com.dm.teamquery.execption.*;
import com.dm.teamquery.entity.Challenge;

import com.dm.teamquery.search.Search;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ChallengeService {


    private final static Logger logger = LogManager.getLogger("ServiceLog");

    @Inject
    private ChallengeRepository challengeRepository;

    @Inject
    private SearchInfoRepository searchInfoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Challenge updateChallenge(Challenge c) throws EntityUpdateException {
        if (null == c.getChallengeId()) c.setChallengeId(UUID.randomUUID());
        try {
            c.setDateLastModified(LocalDateTime.now());
            challengeRepository.save(c);
            return getChallengeById(c.getChallengeId());
        } catch (Exception e) {
            throw new EntityUpdateException(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public String deleteChallengeById(String id)
            throws EntityNotFoundForIdException, InvalidEntityIdException, DeleteFailedException {
        try {
            challengeRepository.deleteById(UUID.fromString(id));
            return "Successfully deleted " + id;
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundForIdException("No entity was found for id: " + id);
        } catch (IllegalArgumentException e) {
            throw new InvalidEntityIdException(ExceptionUtils.getRootCauseMessage(e));
        } catch (Exception e) {
            throw new DeleteFailedException(e.getClass().getSimpleName()
                    + " - " + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    public Challenge getChallengeById(UUID challengeId)
            throws EntityNotFoundForIdException, InvalidEntityIdException, SearchFailedException {
        try {
            return challengeRepository.findById(challengeId).get();
        } catch (NoSuchElementException e) {
            throw new EntityNotFoundForIdException("No entity was found for id: " + challengeId.toString());
        } catch (IllegalArgumentException e) {
            throw new InvalidEntityIdException(ExceptionUtils.getRootCauseMessage(e));
        } catch (Exception e) {
            throw new SearchFailedException(e.getClass().getSimpleName()
                    + " - " + ExceptionUtils.getRootCauseMessage(e));
        }
    }


    public List<Challenge> basicSearch(String query) throws SearchFailedException {
        return (List<Challenge>) search(new SearchRequest(query)).getResultsList();
    }

    public SearchResponse search(SearchRequest request) throws SearchFailedException {

        long startTime = System.nanoTime();
        String query = request.getQuery();
        String dbQuery = prepareQuery(new Search(Challenge.class, query).getDatabaseQuery(), request.getIncDisabled());
        SearchInfo search = new SearchInfo(query, dbQuery);
        SearchResponse response = new SearchResponse(request);

        logger.debug("Processing search request from " + request.getClient_ip() + ", query: " + (query.isEmpty() ? "[none]" : query));

        try {
            response.setRowCount(execCountSearch(dbQuery));
            response.setResultsList(execPagedSearch(dbQuery, request.getPageable()));
            response.setSearchTime((System.nanoTime() - startTime) * 1.0e-9);
            searchInfoRepository.save(search);
            return response;
        } catch (Exception e) {
            search.setErrors(ExceptionUtils.getRootCauseMessage(e));
            searchInfoRepository.save(search);
            throw new SearchFailedException(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private List<Challenge> execPagedSearch(String dbQuery, Pageable p) {
        return entityManager
                .createQuery(dbQuery)
                .setMaxResults(p.getPageSize())
                .setFirstResult(p.getPageNumber() * p.getPageSize())
                .getResultList();
    }

    private long execCountSearch(String dbQuery) {
        return (long) entityManager.createQuery("select count(*) " + dbQuery).getResultList().get(0);
    }

    private String prepareQuery(String query, boolean disabled) {
        String key = disabled ? "0" : "1";
        if (query.equals("from Challenge"))
            return "from Challenge where enabled = " + key;
        else {
            return query.replace("from Challenge where ", "from Challenge where enabled = " + key + " and (") + ")";
        }
    }

}
