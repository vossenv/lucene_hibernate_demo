package com.dm.teamquery.controller;

import com.dm.teamquery.Execption.BadEntityException;
import com.dm.teamquery.Execption.EntityUpdateException;
import com.dm.teamquery.Execption.InvalidParameterException;
import com.dm.teamquery.config.ApiResponseBuilder;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchService;
import com.dm.teamquery.model.Challenge;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RestController
public class APIController {

    @Inject
    private ChallengeService challengeService;

    @Inject
    private SearchService searchService;

    @RequestMapping(value = {"/challenges/update"}, method = RequestMethod.POST)
    public Object addUpdateChallenge(@RequestBody Challenge challenge) throws EntityUpdateException {
        return challengeService.updateChallenge(challenge);
    }

    @RequestMapping(value = {"/challenges/{id}/delete"}, method = RequestMethod.GET)
    public String deleteChallenge(@PathVariable("id") String id)  throws BadEntityException {
        challengeService.deleteChallengeById(id);
        return "Success";
    }

    @RequestMapping(value = {"/challenges/search"}, method = RequestMethod.GET)
    public Object searchChallenge(HttpServletRequest request) throws InvalidParameterException {
       SimplePage p = new SimplePage(request);
       return ApiResponseBuilder.buildApiResponse(challengeService.search(p.getQuery()), p);
    }

    @RequestMapping(value = {"/searches/{id}/delete"}, method = RequestMethod.GET)
    public String deleteSearch(@PathVariable("id") String id) throws BadEntityException {
        searchService.deleteSearchEntityById(id);
        return "Success";
    }

    @RequestMapping(value = {"/searches/search"}, method = RequestMethod.GET)
    public Object searchSearch(HttpServletRequest request) throws InvalidParameterException {
        SimplePage p = new SimplePage(request);
        return ApiResponseBuilder.buildApiResponse(searchService.search(p.getQuery()), p);
    }
}
