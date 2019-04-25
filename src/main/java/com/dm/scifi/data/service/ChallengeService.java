package com.dm.scifi.data.service;

import com.dm.scifi.data.repository.ChallengeRepository;
import com.dm.scifi.entity.Challenge;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.UUID;

@Component
public class ChallengeService extends TeamQueryService<Challenge, UUID> {
    ChallengeService(EntityManager em, ChallengeRepository cr){
        super(em, cr);
    }
}
