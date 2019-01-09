package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    @Inject
    private ChallengeRepository challengeRepository;

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

}
