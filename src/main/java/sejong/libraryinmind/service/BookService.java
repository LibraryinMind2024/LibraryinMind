package sejong.libraryinmind.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.libraryinmind.entity.BookEntity;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.repository.BookRepository;
import sejong.libraryinmind.repository.DiaryRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final DiaryRepository diaryRepository; // DiaryEntity를 조회하기 위해 필요

    @Transactional
    public void saveBook(Map<String, Object> bookData) {
        // Diary ID로 DiaryEntity를 조회
        Long diaryId = Long.valueOf(bookData.get("diaryId").toString());
        DiaryEntity diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("Diary not found for ID: " + diaryId));


        // BookEntity 생성
        BookEntity book = BookEntity.builder()
                .title((String) bookData.get("title"))
                .author(((String) bookData.get("author")).replace("저자: ", "")) // "저자: " 제거
                .summary(((String) bookData.get("summary")).length() > 200
                        ? ((String) bookData.get("summary")).substring(0, 200)
                        : (String) bookData.get("summary")) // summary가 200자 초과 시 200자까지만 저장
                .bookImage((String) bookData.get("bookImage"))
                .bookLink((String) bookData.get("bookLink"))
                .build();


        // Diary와 연관 관계 설정
        book.setDiaryId(diary);

        // 데이터베이스에 저장
        bookRepository.save(book);
    }
}
