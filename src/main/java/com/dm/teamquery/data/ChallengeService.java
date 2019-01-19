package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.search.Search;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    @Inject
    private ChallengeRepository challengeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Challenge updateChallenge(Challenge c) {
        if (null == c.getChallengeId()) c.setChallengeId(UUID.randomUUID());
        return challengeRepository.save(c);
    }

    public void deleteChallengeById(String id){
        challengeRepository.deleteById(UUID.fromString(id));
    }

    public List<Challenge> findAll() {
        List<Challenge> l = new ArrayList<>();
        challengeRepository.findAll().forEach(l::add);
        return l;
    }

    public List<Challenge> search (String query){

        Search s = new Search(Challenge.class, query);

        return entityManager
                .createQuery(s.getDatabaseQuery())
                .getResultList();
    }



}
