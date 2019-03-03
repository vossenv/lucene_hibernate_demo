package com.dm.teamquery.data;


import com.dm.teamquery.data.generic.SearchRequest;
import com.dm.teamquery.data.generic.SearchResponse;
import com.dm.teamquery.data.repository.ChallengeRepository;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.*;
import com.dm.teamquery.search.Search;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {


    private final static Logger logger = LogManager.getLogger("ServiceLog");

    @Inject
    private ChallengeRepository challengeRepository;

//    @Inject
//    private SearchInfoRepository searchInfoRepository;


    public Challenge updateChallenge(Challenge c)
            throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {
        challengeRepository.saveEntity(c);
        return challengeRepository.findEntityById(c.getChallengeId());
    }

    public void deleteChallengeById(UUID id)
            throws EntityNotFoundException, DeleteFailedException {
                challengeRepository.deleteEntityById(id);
    }

    public Challenge getChallengeById(UUID challengeId)
            throws EntityNotFoundException, InvalidEntityIdException, EntityLookupException {
        return challengeRepository.findEntityById(challengeId);
    }

    public List<Challenge> basicSearch(String query) throws SearchFailedException {
        return (List<Challenge>) search(new SearchRequest(query)).getResultsList();
    }

    public SearchResponse search(SearchRequest request) throws SearchFailedException {

        long startTime = System.nanoTime();
        String query = request.getQuery();
        String dbQuery = prepareQuery(new Search(Challenge.class, query).getDatabaseQuery(), request.getIncDisabled());
       // SearchInfo search = new SearchInfo(query, dbQuery);
        SearchResponse response = new SearchResponse(request);

        logger.debug("Processing search request from " + request.getClient_ip() + ", query: " + (query.isEmpty() ? "[none]" : query));

        try {
            response.setRowCount(challengeRepository.count(dbQuery));
            response.setResultsList(challengeRepository.search(dbQuery, request.getPageable()));
            response.setSearchTime((System.nanoTime() - startTime) * 1.0e-9);
        //    searchInfoRepository.save(search);
            return response;
        } catch (Exception e) {
       //     search.setErrors(ExceptionUtils.getRootCauseMessage(e));
       //     searchInfoRepository.save(search);
            throw new SearchFailedException(ExceptionUtils.getRootCauseMessage(e));
        }
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
