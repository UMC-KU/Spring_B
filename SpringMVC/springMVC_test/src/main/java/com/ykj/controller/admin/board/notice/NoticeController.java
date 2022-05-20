package com.ykj.controller.admin.board.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RequestMapping("/admin/board/notice/")
@Controller("adminNoticeController")
public class NoticeController {
    @Autowired
    private ServletContext context;
    @RequestMapping("reg")
    public String reg() {

        return "admin/board/notice/reg";
    }

    @RequestMapping("detail")
    public void detail(HttpServletResponse response, String title, String content, String category, String ch1, String rad1, MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {
            String webPath = "/static/upload";
            String realPath = context.getRealPath(webPath);
            realPath += File.separator+file.getOriginalFilename();

            File saveFile = new File(realPath);

            if(!saveFile.exists()){
                saveFile.mkdirs();
            }
            file.transferTo(saveFile);
        }

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().printf("title : %s<br>content : %s<br>category : %s<br>checkbox : %s<br>radio : %s", title, content,category,ch1,rad1);
    }
}
 