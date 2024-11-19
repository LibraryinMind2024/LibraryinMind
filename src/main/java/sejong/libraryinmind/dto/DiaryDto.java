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
    private String content;
    private LocalDateTime createdDate;

    public DiaryEntity toEntity(){
        DiaryEntity build = DiaryEntity.builder()
                .id(id)
                .content(content)
                .build();
        return build;
    }

    @Builder
    public DiaryDto(Long id, String content, LocalDateTime createdDate){
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
    }

}
