package sejong.libraryinmind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sejong.libraryinmind.entity.UserEntity;
import sejong.libraryinmind.service.AuthService;
import sejong.libraryinmind.service.DiaryService;
import sejong.libraryinmind.service.FlaskService;
import sejong.libraryinmind.service.UserService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/flask")
public class FlaskRestController {

    @Autowired
    private FlaskService flaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private AuthService authService;


    // Flask 서버에서 데이터 가져오기 (JSON 응답)
    @GetMapping("/data")
    public ResponseEntity<String> getDataFromFlask() {
        String data = flaskService.getFlaskData();
        return ResponseEntity.ok(data);
    }

    // Flask 서버로 이미지 업로드 요청 (JSON 응답)
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImageToFlask(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {

        // JWT 토큰에서 username을 추출하여 사용자 인증
        UserEntity user = authService.authenticateFromToken(authorizationHeader);

        if (user == null) {
            // 인증 실패 시 401 Unauthorized 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        Long userId = user.getId();  // 인증된 사용자 정보 사용

        // Flask 응답에서 받은 JSON 데이터를 Map으로 받기
        Map<String, Object> response = flaskService.uploadAndProcessImage(file);

        // Flask 데이터로 DiaryEntity 저장
        diaryService.saveDiaryFromFlaskData(response, user, userId);

        return ResponseEntity.ok(response);
    }

}