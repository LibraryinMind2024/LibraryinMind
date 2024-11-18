package sejong.libraryinmind.dto;

import lombok.*;
import sejong.libraryinmind.entity.DiaryEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryDto {
    private Long id;
    private Long fileId;
    private LocalDateTime createdDate;

    public DiaryEntity toEntity(){
        DiaryEntity build = DiaryEntity.builder()
                .id(id)
                .fileId(fileId)
                .build();
        return build;
    }

    @Builder
    public DiaryDto(Long id,Long fileId, LocalDateTime createdDate){
        this.id = id;
        this.fileId = fileId;
        this.createdDate = createdDate;
    }

}
