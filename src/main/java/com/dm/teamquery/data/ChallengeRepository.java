package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


public interface ChallengeRepository extends CrudRepository<Challenge, UUID> {

    Challenge findChallengeByChallengeId(UUID id);
    Challenge findChallengeByAnswerContains(String ans);


}



