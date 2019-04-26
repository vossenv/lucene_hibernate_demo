package com.dm.scifi.controller.helper;

import com.dm.scifi.controller.MovieController;
import com.dm.scifi.entity.Movie;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
@Relation(collectionRelation = "data")
public class MovieResource extends ResourceSupport {

    private Movie movie;

    public MovieResource(final Movie movie) {
        this.movie = movie;

        try {
            add(linkTo(methodOn(MovieController.class).get(movie.getMovieId().toString())).withSelfRel());
        } catch (Exception e) {
            System.out.println();
        }

    }
}
