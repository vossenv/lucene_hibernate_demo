package com.dm.teamquery.controller;


import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchRequest;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.BadEntityException;
import com.dm.teamquery.execption.EntityUpdateException;
import com.dm.teamquery.execption.InvalidParameterException;
import com.dm.teamquery.execption.SearchFailedException;
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
    public Object get(@PathVariable final String id) {
        return challengeService
                .getChallengeById(UUID.fromString(id))
                .map(p -> ResponseEntity.ok(new ChallengeResource(p)))
                .orElseThrow(EntityNotFoundException::new);
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    public Object addUpdateChallenge(@RequestBody Challenge challenge) throws EntityUpdateException {
        return ResponseEntity.ok(new ChallengeResource(challengeService.updateChallenge(challenge)));
    }

    @RequestMapping(value = {"/{id}/delete"}, method = RequestMethod.GET)
    public String deleteChallenge(@PathVariable("id") String id) throws BadEntityException {
        return challengeService.deleteChallengeById(id);
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public Object searchChallenge(HttpServletRequest request)
            throws InvalidParameterException, SearchFailedException, UnsupportedEncodingException {

        return  challengeService.search(new SearchRequest(request))
                .getResponse(Challenge.class, ChallengeResource.class);

    }
    
}
