package com.dm.teamquery.controller;


import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchRequest;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping(value = "/challenges", produces = "application/hal+json")
public class ChallengeController {

    @Inject
    private ChallengeService challengeService;

    @GetMapping("/{id}")
    public Object get(@PathVariable final String id) throws EntityNotFoundException,
            InvalidEntityIdException, EntityLookupException {
        return ResponseEntity.ok(new ChallengeResource(challengeService.getChallengeById(UUID.fromString(id))));
    }

    @PostMapping(value = {"/update"})
    public Object addUpdateChallenge(@Valid @RequestBody Challenge challenge) throws Exception {
        challengeService.updateChallenge(challenge);
        Challenge z = challengeService.getChallengeById(challenge.getChallengeId());
        return ResponseEntity.ok(new ChallengeResource(challengeService.getChallengeById(challenge.getChallengeId())));
    }

    @DeleteMapping(value = {"/{id}"})
    public Object deleteChallenge(@PathVariable("id") String id) throws EntityNotFoundException, DeleteFailedException {
        challengeService.deleteChallengeById(UUID.fromString(id));

        return ResponseEntity.ok("Success");
    }

    @GetMapping(value = {"/search"})
    public Object searchChallenge(HttpServletRequest request)
            throws InvalidParameterException, SearchFailedException, UnsupportedEncodingException {
        return  challengeService.search(new SearchRequest(request))
                .getResponse(Challenge.class, ChallengeResource.class);

    }
    
}
