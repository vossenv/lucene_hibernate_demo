package com.dm.teamquery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class WebController {

    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public Object indexPage()  {
        return "index.html";
    }

    @ResponseBody
    @RequestMapping(value = {"/status"}, method = RequestMethod.GET)
    public String status() {
        return "true";
    }
}
