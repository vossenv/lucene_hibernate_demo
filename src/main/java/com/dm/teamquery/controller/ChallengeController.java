package com.dm.teamquery.controller;


import com.dm.teamquery.data.ChallengeRepository;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.entity.Challenge;
import com.dm.teamquery.execption.InvalidParameterException;
import com.dm.teamquery.data.SearchResult;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
@RequestMapping(value = "/challenges", produces = "application/hal+json")
public class ChallengeController {

    @Inject
    private ChallengeRepository challengeRepository;

    @Inject
    private ChallengeService challengeService;

    @GetMapping("/{id}")
    public Object get(@PathVariable final String id) {
        return challengeRepository
                .findById(UUID.fromString(id))
                .map(p -> ResponseEntity.ok(new ChallengeResource(p)))
                .orElseThrow(EntityNotFoundException::new);
    }


    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public Object searchChallenge(
            @RequestParam("disabled") Optional<String> disabled,
            HttpServletRequest request) throws InvalidParameterException {

        SimplePage p = new SimplePage(request, disabled);
        SearchResult searchResults =
                challengeService.search(p.getQuery(),p.getPageable(), p.getIncludeDisabled());

        Resources body =
                new Resources<>(searchResults
                .getResultsList().stream()
                .map(c -> new ChallengeResource((Challenge) c))
                .collect(Collectors.toList()));

        return p.prepareResponse(body, searchResults);
    }
    
}
