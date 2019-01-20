package com.dm.teamquery.controller;

import com.dm.teamquery.Execption.InvalidParameterException;
import com.dm.teamquery.config.ApiResponseBuilder;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchService;
import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.model.SimplePage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Inject
    private SearchService searchService;

    @ResponseBody
    @RequestMapping(value = {"/searches/{id}/delete"}, method = RequestMethod.GET)
    public void deleteChallenge(@PathVariable("id") String id)  {
         searchService.deleteSearchEntityById(id);
    }

    @ResponseBody
    @RequestMapping(value = {"/searches/search"}, method = RequestMethod.GET)
    public Object searchChallenge(
            HttpServletRequest request) throws InvalidParameterException {
       SimplePage p = new SimplePage(request);
       return ApiResponseBuilder.buildApiResponse(searchService.search(p.getQuery()), p);
    }
}
