package sejong.libraryinmind.service;


import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.entity.UserEntity;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.repository.UserRepository;
import sejong.libraryinmind.repository.DiaryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryService {

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
    public Long savePost(DiaryDto diaryDto) {
        // 현재 로그인된 사용자의 username 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // username으로 CustomerEntity 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // DiaryEntity 생성 및 customer 설정
        DiaryEntity diaryEntity = diaryDto.toEntity();
        diaryEntity.setUser(user);

        // DiaryEntity 저장
        return diaryRepository.save(diaryEntity).getId();
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
