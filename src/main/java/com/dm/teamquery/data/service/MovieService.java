package com.dm.teamquery.data.service;

import com.dm.teamquery.data.repository.MovieRepository;
import com.dm.teamquery.entity.Movie;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.UUID;

@Component
public class MovieService extends TeamQueryService<Movie, UUID> {
    MovieService(EntityManager em, MovieRepository mr){
        super(em, mr);
    }
}
