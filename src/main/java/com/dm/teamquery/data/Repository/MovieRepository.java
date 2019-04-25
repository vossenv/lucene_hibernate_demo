package com.dm.teamquery.data.repository;

import com.dm.teamquery.entity.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface MovieRepository extends PagingAndSortingRepository<Movie, UUID>{

}



