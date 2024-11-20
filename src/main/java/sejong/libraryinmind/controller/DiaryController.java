package sejong.libraryinmind.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.dto.FileDto;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.service.DiaryService;
import sejong.libraryinmind.service.FileService;
import sejong.libraryinmind.util.MD5Generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class DiaryController {
    private DiaryService diaryService;
    private FileService fileService;

    public DiaryController(DiaryService diaryService, FileService fileService){
        this.diaryService = diaryService;
        this.fileService = fileService;
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
    public String recommend(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String name = (String) session.getAttribute("name");

        if (username != null && name != null) {
            model.addAttribute("username", username);
            model.addAttribute("name", name);

            return "recommend";
        }

        // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }


    @GetMapping("/my_library")
    public String myLibrary(
            @RequestParam(required = false) String date,
            Model model,
            HttpSession session
    ) {
        String username = (String) session.getAttribute("username");
        String name = (String) session.getAttribute("name");

        if (username == null || name == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        model.addAttribute("name", name);

        // 날짜가 선택된 경우 일기 데이터를 가져옴
        if (date != null) {
            Long userId = (Long) session.getAttribute("id");
            List<DiaryEntity> diaries = diaryService.getDiaryByDateAndUserId(date, userId);
            model.addAttribute("diaries", diaries);

            System.out.println("조회된 Diaries:");
            for (DiaryEntity diary : diaries) {
                System.out.println("내용: " + diary.getContent() + ", 작성일: " + diary.getCreatedDate());
            }
        }

        return "my_library";
    }


    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }


    @PostMapping ("/recommend")
    public String SaveDiary(DiaryDto diaryDto, HttpSession session){
        diaryService.saveDiary(diaryDto, session);

        return "redirect:/my_library";
    }

    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id) {
        diaryService.deletePost(id);
        return "redirect:/";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilepath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }
}
