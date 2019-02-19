package com.dm.teamquery.controller;

import com.dm.teamquery.Execption.BadEntityException;
import com.dm.teamquery.Execption.EntityUpdateException;
import com.dm.teamquery.Execption.InvalidParameterException;
import com.dm.teamquery.config.ApiResponseBuilder;
import com.dm.teamquery.config.ExtendedLogger;
import com.dm.teamquery.data.ChallengeService;
import com.dm.teamquery.data.SearchService;
import com.dm.teamquery.model.Challenge;
import com.dm.teamquery.model.ChallengeResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@CrossOrigin
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
public class APIController {

    @Inject private ExtendedLogger el;
    @Inject private ChallengeService challengeService;
    @Inject private SearchService searchService;

    @RequestMapping(value = {"/challenges/update"}, method = RequestMethod.POST)
    public Object addUpdateChallenge(@RequestBody Challenge challenge) throws EntityUpdateException {
        return challengeService.updateChallenge(challenge);
    }

    @RequestMapping(value = {"/challenges/{id}/delete"}, method = RequestMethod.GET)
    public String deleteChallenge(@PathVariable("id") String id) throws BadEntityException {
        return challengeService.deleteChallengeById(id);
    }

    @RequestMapping(value = {"/challenges/{id}"}, method = RequestMethod.GET)
    public Resource<Challenge> searchChallengeById(@PathVariable("id") String id,
            @RequestParam("disabled") Optional<String> disabledMode){


//        Resource<Student> resource = new Resource<Student>(student.get());
//        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllStudents());
//        resource.add(linkTo.withRel("all-students"));

        Optional<Challenge> result = challengeService.getChallengeById(UUID.fromString(id));
        if (result.isPresent() && (result.get().getEnabled() || disabledMode.isPresent())) {
          //  Resource<> res = new Resource<>(result.get());

            Link link = linkTo(this.getClass()).slash("challenges").slash(id).withSelfRel();

            ChallengeResource c = new ChallengeResource(result.get());
          //  c.add(link);
         //  res.add(link);

            Link selfRel = linkTo(APIController.class)
                    .slash("challenges")
                    .slash(result.get().getChallengeId().toString())
                    .withSelfRel();
            Resource<Challenge> x1 = new Resource<>(result.get(),selfRel);
            ResponseEntity x = new ResponseEntity<>(c, new HttpHeaders(), HttpStatus.OK);
            return x1;
        } else {
            return null; //new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


//        Optional<Challenge> result = challengeService.getChallengeById(UUID.fromString(id));
//        return (result.isPresent() && (result.get().getEnabled() || disabledMode.isPresent()))
//                ? ResponseEntity.ok()
//                .body(new Resource<>(result.get())
//                        .add();
//        ) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = {"/challenges/search"}, method = RequestMethod.GET)
    public Object searchChallenge(
            @RequestParam("disabled") Optional<String> disabledMode,
            HttpServletRequest request) throws InvalidParameterException {

        long startTime = System.nanoTime();
        boolean disabled = disabledMode.isPresent() && Boolean.parseBoolean(disabledMode.get());
        SimplePage p = new SimplePage(request);
        return ApiResponseBuilder.buildApiResponse(challengeService.search(p.getQuery(), p.getPageable(), disabled), p, startTime);
    }

    @RequestMapping(value = {"/searches/{id}/delete"}, method = RequestMethod.GET)
    public String deleteSearch(@PathVariable("id") String id) throws BadEntityException {
        return searchService.deleteSearchEntityById(id);
    }

    @RequestMapping(value = {"/searches/search"}, method = RequestMethod.GET)
    public Object searchSearch(HttpServletRequest request) throws InvalidParameterException {
        long startTime = System.nanoTime();
        SimplePage p = new SimplePage(request);
        return ApiResponseBuilder.buildApiResponse(searchService.search(p.getQuery()), p, startTime);
    }
}
