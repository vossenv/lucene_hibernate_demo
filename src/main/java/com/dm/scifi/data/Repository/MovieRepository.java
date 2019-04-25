package com.dm.scifi.data.repository;

import com.dm.scifi.entity.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface MovieRepository extends PagingAndSortingRepository<Movie, UUID>{

}



