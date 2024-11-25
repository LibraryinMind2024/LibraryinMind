package sejong.libraryinmind.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sejong.libraryinmind.dto.FileDto;
import sejong.libraryinmind.entity.FileEntity;
import sejong.libraryinmind.repository.FileRepository;

@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long saveFile(FileDto fileDto){ //업로드한 파일에 대한 정보 기록
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    @Transactional
    public FileDto getFile(Long id){ //id값을 사용하여 파일에 대한 정보 가져옴
        FileEntity fileEntity = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(id)
                .origFilename(fileEntity.getOrigFilename())
                .filename(fileEntity.getFilename())
                .filepath(fileEntity.getFilePath())
                .build();

        return fileDto;
    }

}
