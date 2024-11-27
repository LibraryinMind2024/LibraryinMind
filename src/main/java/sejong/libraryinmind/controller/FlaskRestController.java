package sejong.libraryinmind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.libraryinmind.entity.UserEntity;
import sejong.libraryinmind.service.DiaryService;
import sejong.libraryinmind.service.FlaskService;
import sejong.libraryinmind.service.UserService;

import java.io.IOException;
import java.util.Map;

@RestController
public class FlaskRestController {

    @Autowired
    private FlaskService flaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiaryService diaryService;


    @PostMapping("/recommend")
    public ResponseEntity<Map<String, Object>> uploadImageToFlask(
            @RequestParam("file") MultipartFile file,
            @RequestParam("search-option") String searchOption) throws IOException {

        UserEntity user = userService.getLoggedInUser();

        if (user == null) {
            // 인증 실패 시 401 Unauthorized 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        Long userId = user.getId();  // 인증된 사용자 정보 사용

        // Flask 응답에서 받은 JSON 데이터를 Map으로 받기
        Map<String, Object> response = flaskService.uploadAndProcessImage(file, searchOption);

        System.out.println(response);

        // Flask 데이터로 DiaryEntity 저장
        Long diaryId = diaryService.saveDiaryFromFlaskData(response, user, userId);
        response.put("diaryId", diaryId);

        return ResponseEntity.ok(response);
    }
}