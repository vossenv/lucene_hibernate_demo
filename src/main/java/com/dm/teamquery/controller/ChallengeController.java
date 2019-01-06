package com.dm.teamquery.controller;

import com.dm.teamquery.data.ChallengeDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
public class ChallengeController {

    private final ChallengeDao challengeDao;

    @Inject
    public ChallengeController(ChallengeDao challengeDao) {
        this.challengeDao = challengeDao;
    }

    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public Object indexPage()  {
        return "index.html";
    }

    @ResponseBody
    @RequestMapping(value = {"/challenges"}, method = RequestMethod.GET)
    public Object getAllChallenges()  {
        return challengeDao.findAll();
    }
}
