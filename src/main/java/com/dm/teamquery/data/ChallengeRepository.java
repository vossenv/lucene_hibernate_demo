package com.dm.teamquery.data;

import com.dm.teamquery.model.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;


public interface ChallengeRepository extends PagingAndSortingRepository<Challenge, UUID>, CrudRepository<Challenge, UUID> {

    @Query( "from Challenge  c where c.answer like :term " +
            "or c.question like :term " +
            "or c.author like :term " +
            "or c.lastAuthor like :term " +
            "or c.challengeId like :term" )
    Page<Challenge> search(@Param("term")String term, Pageable pageable);
}



