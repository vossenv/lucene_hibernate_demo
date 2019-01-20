package com.dm.teamquery.data;

import com.dm.teamquery.Execption.BadEntityException;
import com.dm.teamquery.Execption.EntityUpdateException;
import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.model.SearchEntity;
import com.dm.teamquery.search.Search;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    @Inject
    private ChallengeRepository challengeRepository;

    @Inject
    private SearchRepository searchRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Challenge updateChallenge(Challenge c) throws EntityUpdateException {
        if (null == c.getChallengeId()) c.setChallengeId(UUID.randomUUID());
        try {
            c.setDateLastModified(LocalDateTime.now());
            return challengeRepository.save(c);
        } catch (Exception e) {
            throw new EntityUpdateException(e.getMessage());
        }
    }

    public String deleteChallengeById(UUID id) throws BadEntityException{
        return deleteChallengeById(id.toString());
    }

    public String deleteChallengeById(String id) throws BadEntityException{
        try {
            challengeRepository.deleteById(UUID.fromString(id));
            return "Successfully deleted " + id;
        } catch (Exception e) {
            throw new BadEntityException(e.getMessage());
        }
    }

    public List<Challenge> search(Object query) {
        return search(query, PageRequest.of(0, 100), false);
    }

    public List<Challenge> search(Object query, boolean disabled) {
        return search(query, PageRequest.of(0, 100), disabled);
    }

    public List<Challenge> search(Object query, Pageable p) {
        return search(query, p, false);
    }

    public List<Challenge> search(Object query, Pageable p, boolean disabled) {

        List<Challenge> results = new ArrayList<>();
        String dbQuery = new Search(Challenge.class, query.toString()).getDatabaseQuery();
        dbQuery = prepareQuery(dbQuery, disabled);
        SearchEntity entity = new SearchEntity(query.toString(), dbQuery);

        try {
            results = entityManager
                    .createQuery(dbQuery)
                    .setMaxResults(p.getPageSize())
                    .setFirstResult(p.getPageNumber() * p.getPageSize())
                    .getResultList();
        } catch (Exception e) {
            entity.setErrors(e.getMessage());
        }

        try {
            searchRepository.save(entity);
        } catch (Exception e) {
            // Do nothing -- non-critical function.  Add logging later
        }
        return results;
    }

    private String prepareQuery(String query, boolean disabled){
        String key = disabled ? "0" : "1";
        if (query.equals("from Challenge"))
            return "from Challenge where enabled = " + key;
        else {
            return query.replace("from Challenge where ", "from Challenge where enabled = " + key + " and (") + ")";
        }
    }

}
