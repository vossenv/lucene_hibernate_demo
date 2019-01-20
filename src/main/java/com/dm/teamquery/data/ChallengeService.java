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
            return challengeRepository.save(c);
        } catch (Exception e) {
            throw new EntityUpdateException(e.getMessage());
        }
    }

    public void deleteChallengeById(String id) throws BadEntityException{
        try {
            challengeRepository.deleteById(UUID.fromString(id));
        } catch (Exception e) {
            throw new BadEntityException(e.getMessage());
        }
    }

    public List<Challenge> search(String query) {
        return search(query, PageRequest.of(0, 100));
    }

    public List<Challenge> search(String query, Pageable p) {

        List<Challenge> results = new ArrayList<>();
        String dbQuery = new Search(Challenge.class, query).getDatabaseQuery();
        SearchEntity entity = new SearchEntity(query, dbQuery);

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
}
