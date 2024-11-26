package sejong.libraryinmind.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookController {

    @PostMapping("/selected-book")
    public ResponseEntity<Map<String, String>> handleSelectedBook(@RequestBody Map<String, Object> bookData) {
        // 받은 데이터 출력
        System.out.println("선택된 도서: " + bookData);

        // 응답 생성
        Map<String, String> response = new HashMap<>();
        response.put("message", "도서 데이터가 성공적으로 수신되었습니다.");
        return ResponseEntity.ok(response);
    }
}
