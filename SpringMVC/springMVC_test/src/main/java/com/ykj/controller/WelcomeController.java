package com.ykj.controller;

import com.ykj.entity.Notice;
import com.ykj.service.NoticeService;
import com.ykj.service.jdbc.JDBCNoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/")
@Controller("welcomeController")
public class WelcomeController {

    @RequestMapping("welcome")
    public String welcome() {
        return "root.welcome";
    }

    @RequestMapping("hello")
    @ResponseBody
    public List<Notice> hello() {
        NoticeService service = new JDBCNoticeService();

        return service.getNoticeList();
    }
}
