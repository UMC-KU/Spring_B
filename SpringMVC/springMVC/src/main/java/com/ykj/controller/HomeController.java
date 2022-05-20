package com.ykj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("/")
public class HomeController {
//    @Override
//    public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse) throws Exception {
//        ModelAndView mv = new ModelAndView("root.index");
//        System.out.println("Dfs");
//        mv.addObject("data", "Hello Spring MVC");
//
//        return mv;
    @RequestMapping("index")
    public String index() {
        return "root.index";
    }

    @RequestMapping("hello")
    @ResponseBody
    public void hello(@RequestParam(name="p", defaultValue = "1") int page, HttpServletResponse response) throws IOException {
        response.getWriter().println(page);
    }
}
