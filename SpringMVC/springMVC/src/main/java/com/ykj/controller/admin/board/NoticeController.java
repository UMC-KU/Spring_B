package com.ykj.controller.admin.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller("adminNoticeController")
@RequestMapping("/admin/board/notice/")
public class NoticeController {
    @RequestMapping("list")
    public void list(){

    }
    @RequestMapping("edit")
    public String reg(HttpServletRequest request) throws UnsupportedEncodingException {
        return "admin.board.notice.edit";
    }
    @PostMapping(value = "reg")
    public String reg(String title, String content, String category, @RequestParam(required = false) String[] food, MultipartFile[] files, HttpServletRequest request) throws IOException {
        for (MultipartFile file : files) {
            String webPath = "/static/upload";
            String realPath = request.getServletContext().getRealPath(webPath);
            realPath += File.separator + file.getOriginalFilename();
            File saveFile = new File(realPath);

            file.transferTo(saveFile);
        }

        return "redirect:list";
    }

    @GetMapping(value = "reg")
    public String reg() {
        return "admin.board.notice.reg";
    }
    @RequestMapping("del")
    public String del(){

        return "admin.board.notice.del";
    }
}
