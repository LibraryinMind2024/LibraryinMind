package sejong.libraryinmind.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.libraryinmind.service.BookService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @PostMapping("/selected-book")
    public ResponseEntity<Map<String, String>> handleSelectedBook(@RequestBody Map<String, Object> bookData) {
        try {
            // 서비스 호출하여 데이터 저장
            bookService.saveBook(bookData);

            // 응답 생성
            Map<String, String> response = new HashMap<>();
            response.put("message", "도서가 저장되었습니다. 도서함에서 확인해보세요!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 처리
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "도서 저장 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
