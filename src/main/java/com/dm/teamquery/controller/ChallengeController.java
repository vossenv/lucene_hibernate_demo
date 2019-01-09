package com.dm.teamquery.controller;

import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.model.Challenge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
public class ChallengeController {

    @Inject
    private ChallengeService challengeService;

    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public Object indexPage()  {
        return "index.html";
    }

    @ResponseBody
    @RequestMapping(value = {"/challenges"}, method = RequestMethod.GET)
    public Object getAllChallenges()  {
        return challengeService.findAll();
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

}
