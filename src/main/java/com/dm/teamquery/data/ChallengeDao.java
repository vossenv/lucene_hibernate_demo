package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChallengeDao {

    private final ChallengeRepository challengeRepository;

    @Inject
    public ChallengeDao(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    public Challenge findChallengeByChallengeId(int id) {
        return challengeRepository.findChallengeByChallengeId(id);
    }

    public Challenge findChallengeByAnswerContains(String ans) {
        return challengeRepository.findChallengeByAnswerContains(ans);
    }

    public List<Challenge> findAll(){
        List<Challenge> l = new ArrayList<>();
        challengeRepository.findAll().forEach(l::add);
        return l;
    }



}
