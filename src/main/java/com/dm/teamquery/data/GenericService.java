//package com.dm.teamquery.data;
//
//
//import com.dm.teamquery.data.generic.SearchRequest;
//import com.dm.teamquery.data.generic.SearchResponse;
//import com.dm.teamquery.data.repository.custom.TeamQueryRepository;
//import com.dm.teamquery.data.repository.SearchInfoRepository;
//import com.dm.teamquery.entity.Challenge;
//import com.dm.teamquery.entity.SearchInfo;
//import com.dm.teamquery.execption.*;
//import com.dm.teamquery.execption.EntityNotFoundException;
//import com.dm.teamquery.search.Search;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.metadata.ClassMetadata;
//import org.springframework.stereotype.Service;
//
//import javax.inject.Inject;
//import javax.persistence.*;
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//@Service
//public class GenericService<T> {
//
//
//    private final static Logger logger = LogManager.getLogger("ServiceLog");
//
//    @Inject
//    private TeamQueryRepository<T, UUID> repository;
//
//    @Inject
//    private SearchInfoRepository searchInfoRepository;
//
//    @PersistenceContext
//    EntityManagerFactory em;
//
//
//    public T update (T t)
//            throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {
//
//
//        PersistenceUnitUtil util = em.getPersistenceUnitUtil();
//        Object projectId = util.getIdentifier(t);
//
//        repository.saveEntity(t);
//        return repository.findEntityById(((Challenge) t).getChallengeId());
//    }
//
//    public void deleteById(UUID id)
//            throws EntityNotFoundException, DeleteFailedException {
//                repository.deleteEntityById(id);
//    }
//
//    public T getById(UUID challengeId)
//            throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {
//        return repository.findEntityById(challengeId);
//    }
//
//    public List<Challenge> basicSearch(String query) throws SearchFailedException {
//        return (List<Challenge>) search(new SearchRequest(query)).getResultsList();
//    }
//
//    public SearchResponse search(SearchRequest request) throws SearchFailedException {
//
//        long startTime = System.nanoTime();
//        String query = request.getQuery();
//        String dbQuery = prepareQuery(new Search(Challenge.class, query).getDatabaseQuery(), request.getIncDisabled());
//        SearchInfo search = new SearchInfo(query, dbQuery);
//        SearchResponse response = new SearchResponse(request);
//
//        logger.debug("Processing search request from " + request.getClient_ip() + ", query: " + (query.isEmpty() ? "[none]" : query));
//
//        try {
//            response.setRowCount(repository.count(dbQuery));
//            response.setResultsList(repository.search(dbQuery, request.getPageable()));
//            response.setSearchTime((System.nanoTime() - startTime) * 1.0e-9);
//            searchInfoRepository.save(search);
//            return response;
//        } catch (Exception e) {
//            search.setErrors(ExceptionUtils.getRootCauseMessage(e));
//            searchInfoRepository.save(search);
//            throw new SearchFailedException(ExceptionUtils.getRootCauseMessage(e));
//        }
//    }
//
//    private String prepareQuery(String query, boolean disabled) {
//        String key = disabled ? "0" : "1";
//        if (query.equals("from Challenge"))
//            return "from Challenge where enabled = " + key;
//        else {
//            return query.replace("from Challenge where ", "from Challenge where enabled = " + key + " and (") + ")";
//        }
//    }
//
//
//
//}
