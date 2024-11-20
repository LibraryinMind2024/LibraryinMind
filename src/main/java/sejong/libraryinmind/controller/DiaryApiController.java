package sejong.libraryinmind.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.service.DiaryService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
public class DiaryApiController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping("/my_library/api")
    public List<DiaryEntity> getDiaries(@RequestParam("date") String date, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String name = (String) session.getAttribute("name");

        if (username == null || name == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }

        Long userId = (Long) session.getAttribute("id");
        List<DiaryEntity> diaries = diaryService.getDiaryByDateAndUserId(date, userId);
        return diaries; // JSON 형식으로 반환
    }
}