package com.ykj.controller.api;

import com.ykj.entity.Notice;
import com.ykj.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notice/")
public class NoticeController {
    @Autowired                  
    private NoticeService service;

    @RequestMapping("list")
    public Notice list() {

        List<Notice> list = service.getNoticeList();
        return list.get(0);
    }
}
