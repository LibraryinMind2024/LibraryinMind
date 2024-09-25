package sejong.libraryinmind.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sejong.libraryinmind.controller.DiaryController;
import sejong.libraryinmind.dto.DiaryDto;
import sejong.libraryinmind.entity.DiaryEntity;
import sejong.libraryinmind.repository.DiaryRepository;
import sejong.libraryinmind.repository.FileRepository;

@Service
public class DiaryService {
    private DiaryRepository diaryRepository;
    public DiaryService(DiaryRepository diaryRepository){
        this.diaryRepository = diaryRepository;
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
                .build();

        return diaryDto;
    }

}
