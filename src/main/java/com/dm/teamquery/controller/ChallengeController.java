package com.dm.teamquery.controller;

import com.dm.teamquery.Execption.InvalidParameterException;
import com.dm.teamquery.config.ApiResponseBuilder;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.model.SimplePage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ChallengeController {

    @Inject
    private ChallengeService challengeService;

    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public Object indexPage()  {
        return "index.html";
    }

    @ResponseBody
    @RequestMapping(value = {"/challenges/update"}, method = RequestMethod.POST)
    public Object addUpdateChallenge(@RequestBody Challenge challenge)  {
        return challengeService.updateChallenge(challenge);
    }

    @ResponseBody
    @RequestMapping(value = {"/challenges/{id}/delete"}, method = RequestMethod.GET)
    public void deleteChallenge(@PathVariable("id") String id)  {
         challengeService.deleteChallengeById(id);
    }

    @ResponseBody
    @RequestMapping(value = {"/challenges/search"}, method = RequestMethod.GET)
    public Object searchChallenge(
            HttpServletRequest request) throws InvalidParameterException {

       SimplePage p = new SimplePage(request);
       return ApiResponseBuilder.buildApiResponse(challengeService.search(p.getQuery()), p);

    }
}
