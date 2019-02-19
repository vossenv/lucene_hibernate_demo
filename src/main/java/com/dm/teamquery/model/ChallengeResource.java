package com.dm.teamquery.model;

import com.dm.teamquery.controller.APIController;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter @Setter
@EqualsAndHashCode(callSuper = false)
public class ChallengeResource extends ResourceSupport {


    private Challenge challenge;

    public ChallengeResource(Challenge challenge){
        this.challenge = challenge;

        add(linkTo(APIController.class)
                .slash("challenges")
                .slash(challenge.getChallengeId().toString())
                .withSelfRel());

        add(linkTo(APIController.class)
                .slash("/challenges/search")
                .withRel("search"));
    }


}
