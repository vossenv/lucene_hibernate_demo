package com.dm.scifi.data.repository;

import com.dm.scifi.entity.Challenge;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface ChallengeRepository extends PagingAndSortingRepository<Challenge, UUID>{

}



