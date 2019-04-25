package com.dm.scifi.controller;

import com.dm.scifi.entity.Challenge;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Getter
@Relation(collectionRelation = "data")
public class ChallengeResource extends ResourceSupport {

    private com.dm.scifi.entity.Challenge challenge;

    public ChallengeResource(final Challenge challenge) {
        this.challenge = challenge;

        try {
//            add(linkTo(methodOn(ChallengeController.class).searchChallenge(null)).withRel("search"));
//            add(linkTo(methodOn(ChallengeController.class).get(challenge.getChallengeId().toString())).withSelfRel());

        } catch (Exception e) {
            System.out.println();
        }

    }
}
