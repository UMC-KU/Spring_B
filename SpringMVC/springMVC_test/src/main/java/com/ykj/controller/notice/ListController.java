package com.ykj.controller.notice;


import com.ykj.service.NoticeService;
import com.ykj.service.jdbc.JDBCNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ListController implements Controller {

    @Autowired
    private NoticeService service;

    public void setService(NoticeService service) {
        this.service = service;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {


        ModelAndView mv = new ModelAndView("notice.list");
        mv.addObject("noticeList", service.getNoticeList());
        return mv;
    }
}
