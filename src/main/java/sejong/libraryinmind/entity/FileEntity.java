package sejong.libraryinmind.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origFilename; //원본 파일명

    @Column(nullable = false)
    private String filename; //서버에 저장되는 파일명

    @Column(nullable = false)
    private String filePath; //서버에 저장되는 경로

    @Builder
    public FileEntity(Long id, String origFilename, String filename, String filePath) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }

}
