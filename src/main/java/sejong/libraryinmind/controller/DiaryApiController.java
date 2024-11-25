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
import sejong.libraryinmind.service.UserService;

import java.util.List;

@RestController
public class DiaryApiController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private UserService userService;

    @GetMapping("/my_library/api")
    public List<DiaryEntity> getDiaries(@RequestParam("date") String date) {
        String username = userService.getLoggedInUsername();
        String name = userService.getLoggedInName();
        Long userId = userService.getLoggedInId();

        System.out.println(username);
        System.out.println(name);
        System.out.println(userId);


        if (username == null || name == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
        }

        List<DiaryEntity> diaries = diaryService.getDiaryByDateAndUserId(date, userId);
        return diaries; // JSON 형식으로 반환
    }
}