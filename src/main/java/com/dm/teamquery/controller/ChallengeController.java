package com.dm.teamquery.controller;


import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchRequest;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping(value = "/challenges", produces = "application/hal+json")
public class ChallengeController {

    @Inject
    private ChallengeService challengeService;

    @GetMapping("/{id}")
    public Object get(@PathVariable final String id) throws EntityNotFoundForIdException,
            InvalidEntityIdException, SearchFailedException {
        return ResponseEntity.ok(new ChallengeResource(challengeService.getChallengeById(UUID.fromString(id))));
    }

    @PostMapping(value = {"/update"})
    public Object addUpdateChallenge(@RequestBody Challenge challenge) throws EntityUpdateException {
        return ResponseEntity.ok(new ChallengeResource(challengeService.updateChallenge(challenge)));
    }

    @DeleteMapping(value = {"/{id}"})
    public Object deleteChallenge(@PathVariable("id") String id) throws EntityNotFoundForIdException,
            InvalidEntityIdException, DeleteFailedException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(challengeService.deleteChallengeById(id));
        return ResponseEntity.ok(jsonInString);
    }


    @GetMapping(value = {"/search"})
    public Object searchChallenge(HttpServletRequest request)
            throws InvalidParameterException, SearchFailedException, UnsupportedEncodingException {

        return  challengeService.search(new SearchRequest(request))
                .getResponse(Challenge.class, ChallengeResource.class);

    }
    
}
