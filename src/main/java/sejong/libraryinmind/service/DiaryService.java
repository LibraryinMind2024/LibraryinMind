package sejong.libraryinmind.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.entity.UserEntity;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.repository.UserRepository;
import sejong.libraryinmind.repository.DiaryRepository;
import jakarta.servlet.http.HttpSession;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryService {

    @Autowired
    private UserRepository userRepository;
    private DiaryRepository diaryRepository;
    public DiaryService(DiaryRepository diaryRepository){
        this.diaryRepository = diaryRepository;
    }

    public List<DiaryDto> getList(){
        List<DiaryEntity> diaryEntityList = diaryRepository.findAll();
        List<DiaryDto> diaryDtoList = new ArrayList<>();

        for(DiaryEntity diaryEntity : diaryEntityList){
            DiaryDto diaryDto = DiaryDto.builder()
                    .id(diaryEntity.getId())
                    .createdDate(diaryEntity.getCreatedDate())
                    .build();
            diaryDtoList.add(diaryDto);
        }

        return diaryDtoList;
    }

    @Transactional
    public Long saveDiary(DiaryDto diaryDto, HttpSession session) {
        // 세션에서 username 가져오기
        String username = (String) session.getAttribute("username");

        if (username == null) {
            throw new IllegalStateException("User not logged in");
        }

        // username으로 UserEntity 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // DiaryEntity 생성 및 customer 설정
        DiaryEntity diaryEntity = diaryDto.toEntity();
        diaryEntity.setUser(user);

        // DiaryEntity 저장
        return diaryRepository.save(diaryEntity).getId();
    }


    public List<DiaryEntity> getDiaryByDateAndUserId(String date, Long userId) {
        // 입력받은 날짜를 LocalDateTime으로 변환
        LocalDate localDate = LocalDate.parse(date); // "2024-11-19" 형식 가정
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        // 날짜 범위 쿼리를 사용하여 데이터 조회
        List<DiaryEntity> diaries = diaryRepository.findByCreatedDateBetweenAndUserId(startOfDay, endOfDay, userId);

        // 결과 출력
        if (diaries.isEmpty()) {
            System.out.println("No diaries found for user " + userId + " on date " + date);
        } else {
            System.out.println("Found " + diaries.size() + " diaries for user " + userId + " on date " + date);
            for (DiaryEntity diary : diaries) {
                System.out.println(diary);
            }
        }

        return diaries;
    }

    @Transactional
    public DiaryDto getPost(Long id){
        DiaryEntity diaryEntity = diaryRepository.findById(id).get();

        DiaryDto diaryDto = DiaryDto.builder()
                .id(diaryEntity.getId())
                .createdDate(diaryEntity.getCreatedDate())
                .build();

        return diaryDto;
    }

    @Transactional
    public void deletePost(Long id) {
        diaryRepository.deleteById(id);
    }

}
