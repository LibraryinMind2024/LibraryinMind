package sejong.libraryinmind.service;


import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sejong.libraryinmind.controller.DiaryController;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.entity.CustomerEntity;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.repository.CustomerRepository;
import sejong.libraryinmind.repository.DiaryRepository;
import sejong.libraryinmind.repository.FileRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryService {

    private CustomerRepository customerRepository;
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
        CustomerEntity customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // DiaryEntity 생성 및 customer 설정
        DiaryEntity diaryEntity = diaryDto.toEntity();
        diaryEntity.setCustomer(customer);

        // DiaryEntity 저장
        return diaryRepository.save(diaryEntity).getId();
    }


    @Transactional
    public DiaryDto getPost(Long id){
        DiaryEntity diaryEntity = diaryRepository.findById(id).get();

        DiaryDto diaryDto = DiaryDto.builder()
                .id(diaryEntity.getId())
                .fileId(diaryEntity.getFileId())
                .createdDate(diaryEntity.getCreatedDate())
                .build();

        return diaryDto;
    }

    @Transactional
    public void deletePost(Long id) {
        diaryRepository.deleteById(id);
    }

}
