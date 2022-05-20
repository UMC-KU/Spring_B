package com.ykj.controller.notice;

import com.ykj.entity.Notice;
import com.ykj.service.NoticeService;
import com.ykj.service.jdbc.JDBCNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ListController implements Controller {

    private NoticeService service;

    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mv = new ModelAndView("notice.list");

        List<Notice> noticeList = service.getNoticeList();
        mv.addObject("noticeList", noticeList);
        System.out.println(service.toString());

        return mv;
    }

    @Autowired
    public void setService(NoticeService service) {
        this.service = service;
    }
}
