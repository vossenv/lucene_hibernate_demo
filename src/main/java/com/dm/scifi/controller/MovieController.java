package com.dm.scifi.controller;


import com.dm.scifi.controller.helper.MovieResource;
import com.dm.scifi.controller.helper.SearchRequest;
import com.dm.scifi.data.MovieService;
import com.dm.scifi.entity.Movie;
import com.dm.scifi.execption.InvalidParameterException;
import com.dm.scifi.execption.SearchFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/movies", produces = "application/hal+json")
public class MovieController {

    @Inject
    private MovieService movieService;

    @GetMapping("/{id}")
    public Object get(@PathVariable String id)  {
        return ResponseEntity.ok(new MovieResource(movieService.findById(UUID.fromString(id))));
    }

    @PostMapping(value = {"/update"})
    public Object addUpdateChallenge(@Valid @RequestBody Movie movie){
        return ResponseEntity.ok(new MovieResource(movieService.save(movie)));
    }

    @DeleteMapping(value = {"/{id}"})
    public Object deleteChallenge(@PathVariable("id") UUID id){
        movieService.deleteById(id);
        return ResponseEntity.ok("Success");
    }

    @GetMapping(value = {"/search"})
    public Object searchChallenge(HttpServletRequest request)
            throws InvalidParameterException, SearchFailedException, UnsupportedEncodingException {
        return  movieService.search(new SearchRequest(request))
                .getResponse(Movie.class, MovieResource.class);

    }

}
