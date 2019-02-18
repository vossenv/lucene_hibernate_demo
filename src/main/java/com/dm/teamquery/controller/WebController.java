package com.dm.teamquery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class WebController {

    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public Object indexPage(Model model)  {

        model.addAttribute("ctx", "teamquery");
        return "index.html";
    }

    @RequestMapping(value = {"/challenge"}, method = RequestMethod.GET)
    public Object challengePage(Model model)  {

        model.addAttribute("ctx", "teamquery");
        model.addAttribute("challenge", "true");
        return "index.html";
    }

    @ResponseBody
    @RequestMapping(value = {"/status"}, method = RequestMethod.GET)
    public String status() {
        return "true";
    }
}
