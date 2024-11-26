package sejong.libraryinmind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.entity.UserEntity;
import sejong.libraryinmind.service.DiaryService;
import sejong.libraryinmind.service.UserService;

import java.util.List;

@Controller
public class DiaryController {
    private DiaryService diaryService;

    @Autowired
    private UserService userService;

    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }

    @GetMapping("/diary")
    public String diarylist(Model model){

        List<DiaryDto> diaryDtoList = this.diaryService.getList();
        model.addAttribute("diaryDtoList",diaryDtoList);

        return "Diarylist.html";
    }

    @GetMapping("/post")
    public String diary(){
        return "Posting";
    }

    @GetMapping("/recommend")
    public String recommend(Model model) {
        UserEntity user = userService.getLoggedInUser();

        if (user != null) {
            model.addAttribute("username", userService.getLoggedInUsername());
            model.addAttribute("name", userService.getLoggedInName());

            return "upload";
        }

        // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }


    @GetMapping("/my_library")
    public String myLibrary(
            @RequestParam(required = false) String date,
            Model model
    ) {

        UserEntity user = userService.getLoggedInUser();

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", userService.getLoggedInUsername());
        model.addAttribute("name", userService.getLoggedInName());

        // 날짜가 선택된 경우 일기 데이터를 가져옴
        if (date != null) {
            Long userId = userService.getLoggedInId();
            List<DiaryEntity> diaries = diaryService.getDiaryByDateAndUserId(date, userId);
            model.addAttribute("diaries", diaries);

            System.out.println("조회된 Diaries:");
            for (DiaryEntity diary : diaries) {
                System.out.println("내용: " + diary.getImageUrl() + ", 작성일: " + diary.getCreatedDate());
            }
        }

        return "my_library";
    }

    @PostMapping("/recommend")
    public String SaveDiary(DiaryDto diaryDto){
        UserEntity user = userService.getLoggedInUser();

        diaryService.saveDiary(diaryDto, user);

        return "redirect:/my_library";
    }
}
