package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.data.repository.CrudRepository;


public interface ChallengeRepository extends CrudRepository<Challenge, Integer> {

    Challenge findChallengeByChallengeId(int id);
    Challenge findChallengeByAnswerContains(String ans);

}



