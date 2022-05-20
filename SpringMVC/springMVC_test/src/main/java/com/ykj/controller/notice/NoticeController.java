package com.ykj.controller.notice;

import com.ykj.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("customer/notice/")
@Controller("noticeController")
public class NoticeController {

    @Autowired
    private NoticeService service;

    @RequestMapping("list")
    public String noticeList(HttpServletRequest request){
        request.setAttribute("noticeList", service.getNoticeList());
        return "notice.list";
    }
    @RequestMapping("detail")
    public String noticeDetail(){

        return "notice.detail";
    }
}
