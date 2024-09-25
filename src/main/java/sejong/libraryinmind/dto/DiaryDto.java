package sejong.libraryinmind.dto;

import lombok.*;
import sejong.libraryinmind.entity.DiaryEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryDto {
    private Long id;
    private String keyword_1;
    private String keyword_2;
    private String keyword_3;
    private Long fileId;

    public DiaryEntity toEntity(){
        DiaryEntity build = DiaryEntity.builder()
                .id(id)
                .keyword_1(keyword_1)
                .keyword_2(keyword_2)
                .keyword_3(keyword_3)
                .fileId(fileId)
                .build();
        return build;
    }

    @Builder
    public DiaryDto(Long id, String keyword_1, String keyword_2, String keyword_3,Long fileId){
        this.id = id;
        this.keyword_1 = keyword_1;
        this.keyword_2 = keyword_2;
        this.keyword_3 = keyword_3;
        this.fileId = fileId;
    }

}
