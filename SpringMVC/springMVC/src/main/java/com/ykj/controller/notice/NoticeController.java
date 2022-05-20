package com.ykj.controller.notice;

import com.ykj.entity.Notice;
import com.ykj.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("custNoticeController")
@RequestMapping("/customer/notice/")
public class NoticeController {
    @Autowired
    private NoticeService service;

    @RequestMapping("list")
    public String noticeList() {

        List<Notice> noticeList = service.getNoticeList();
        return "notice.list";
    }
    @RequestMapping("detail")
    public String noticeDetail() {


        return "notice.detail";
    }
}
