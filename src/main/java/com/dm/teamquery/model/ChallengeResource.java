package com.dm.teamquery.model;

import com.dm.teamquery.controller.APIController;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Data
public class ChallengeResource extends ResourceSupport {


    private Challenge challenge;

    public ChallengeResource(Challenge challenge){
        this.challenge = challenge;

        add(linkTo(APIController.class)
                .slash("challenges")
                .slash(challenge.getChallengeId().toString())
                .withSelfRel());
    }


}
