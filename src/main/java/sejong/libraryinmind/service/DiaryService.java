package sejong.libraryinmind.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sejong.libraryinmind.controller.DiaryController;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.entity.CustomerEntity;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.repository.DiaryRepository;
import sejong.libraryinmind.repository.FileRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryService {
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
                    .keyword_1(diaryEntity.getKeyword_1())
                    .keyword_2(diaryEntity.getKeyword_2())
                    .keyword_3(diaryEntity.getKeyword_3())
                    .createdDate(diaryEntity.getCreatedDate())
                    .build();
            diaryDtoList.add(diaryDto);
        }

        return diaryDtoList;
    }

    @Transactional
    public Long savePost(DiaryDto diaryDto){
        return diaryRepository.save(diaryDto.toEntity()).getId();
    }

    @Transactional
    public DiaryDto getPost(Long id){
        DiaryEntity diaryEntity = diaryRepository.findById(id).get();

        DiaryDto diaryDto = DiaryDto.builder()
                .id(diaryEntity.getId())
                .keyword_1(diaryEntity.getKeyword_1())
                .keyword_2(diaryEntity.getKeyword_2())
                .keyword_3(diaryEntity.getKeyword_3())
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
