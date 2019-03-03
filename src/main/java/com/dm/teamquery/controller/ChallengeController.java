package com.dm.teamquery.controller;


import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.generic.SearchRequest;
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
    public Object get(@PathVariable String id) throws EntityNotFoundException,
            InvalidEntityIdException, EntityLookupException {
        return ResponseEntity.ok(new ChallengeResource(challengeService.getChallengeById(UUID.fromString(id))));
    }

    @PostMapping(value = {"/update"})
    public Object addUpdateChallenge(@Valid @RequestBody Challenge challenge) throws Exception {
        return ResponseEntity.ok(new ChallengeResource(challengeService.updateChallenge(challenge)));
    }

    @DeleteMapping(value = {"/{id}"})
    public Object deleteChallenge(@PathVariable("id") UUID id) throws EntityNotFoundException, DeleteFailedException {
        challengeService.deleteChallengeById(id);
        return ResponseEntity.ok("Success");
    }

    @GetMapping(value = {"/search"})
    public Object searchChallenge(HttpServletRequest request)
            throws InvalidParameterException, SearchFailedException, UnsupportedEncodingException, EntityNotFoundException {
        return  challengeService.search(new SearchRequest(request))
                .getResponse(Challenge.class, ChallengeResource.class);

    }
    
}
