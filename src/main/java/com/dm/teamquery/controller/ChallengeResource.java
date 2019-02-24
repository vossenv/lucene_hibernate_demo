package com.dm.teamquery.controller;

import com.dm.teamquery.controller.ChallengeController;
import com.dm.teamquery.entity.Challenge;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class ChallengeResource extends ResourceSupport {

  private final com.dm.teamquery.entity.Challenge Challenge;

  public ChallengeResource(final Challenge Challenge) {
    this.Challenge = Challenge;
    add(linkTo(ChallengeController.class).withRel("people"));
    add(linkTo(methodOn(ChallengeController.class).get(Challenge.getChallengeId().toString())).withSelfRel());
  }
}
