package com.dm.scifi.data;

import com.dm.scifi.entity.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface MovieRepository extends PagingAndSortingRepository<Movie, UUID>{

}



