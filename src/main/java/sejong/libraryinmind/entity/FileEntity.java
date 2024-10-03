package sejong.libraryinmind.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_origname")
    private String origFilename; //원본 파일명

    @Column(name = "file_name", nullable = false)
    private String filename; //서버에 저장되는 파일명

    @Column(name = "file_path", nullable = false)
    private String filePath; //서버에 저장되는 경로

    @Builder
    public FileEntity(Long id, String origFilename, String filename, String filePath) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
    }

}
