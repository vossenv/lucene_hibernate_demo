package com.dm.teamquery.controller;

import com.dm.teamquery.Execption.BadEntityException;
import com.dm.teamquery.Execption.EntityUpdateException;
import com.dm.teamquery.Execption.InvalidParameterException;
import com.dm.teamquery.config.ApiResponseBuilder;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchService;
import com.dm.teamquery.model.Challenge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class APIController {

    @Inject
    private ChallengeService challengeService;

    @Inject
    SearchService searchService;

    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public Object indexPage()  {
        return "index.html";
    }

    @ResponseBody
    @RequestMapping(value = {"/challenges/update"}, method = RequestMethod.POST)
    public Object addUpdateChallenge(@RequestBody Challenge challenge) throws EntityUpdateException {
        try {
            return challengeService.updateChallenge(challenge);
        } catch (Exception e){
            throw new EntityUpdateException(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/challenges/{id}/delete"}, method = RequestMethod.GET)
    public String deleteChallenge(@PathVariable("id") String id)  throws BadEntityException {
        try {
            challengeService.deleteChallengeById(id);
            return "Success";
        } catch (Exception e) {
            throw new BadEntityException(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/challenges/search"}, method = RequestMethod.GET)
    public Object searchChallenge(
            HttpServletRequest request) throws InvalidParameterException {
       SimplePage p = new SimplePage(request);
       return ApiResponseBuilder.buildApiResponse(challengeService.search(p.getQuery()), p);
    }

    @ResponseBody
    @RequestMapping(value = {"/searches/{id}/delete"}, method = RequestMethod.GET)
    public String deleteSearch(@PathVariable("id") String id) throws BadEntityException {
        try {
            searchService.deleteSearchEntityById(id);
            return "Success";
        } catch (Exception e) {
            throw new BadEntityException(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/searches/search"}, method = RequestMethod.GET)
    public Object searchSearch(
            HttpServletRequest request) throws InvalidParameterException {
        SimplePage p = new SimplePage(request);
        return ApiResponseBuilder.buildApiResponse(searchService.search(p.getQuery()), p);
    }
}
