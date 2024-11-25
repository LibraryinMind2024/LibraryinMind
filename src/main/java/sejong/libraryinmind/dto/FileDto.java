package sejong.libraryinmind.dto;


import lombok.*;
import sejong.libraryinmind.entity.FileEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String origFilename;
    private String filename;
    private String filepath;

    public FileEntity toEntity(){
        FileEntity build = FileEntity.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filepath)
                .build();

        return build;
    }

    @Builder
    public FileDto(Long id, String origFilename, String filename, String filepath){
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filepath = filepath;
    }

}
