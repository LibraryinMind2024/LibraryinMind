package sejong.libraryinmind.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT", length = 500)
    private String summary;

    @Column(name = "book_image")
    private String bookImage;

    @Column(name = "book_link")
    private String bookLink;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore  // 직렬화에서 제외
    @JoinColumn(name = "diary_id", referencedColumnName = "id", unique = true, foreignKey = @ForeignKey(name = "fk_diary"))
    private DiaryEntity diary;

    @Builder
    public BookEntity(Long id, String title, String author,
                      String summary, String bookLink, String bookImage){
        this.id = id;
        this.title = title;
        this.author = author;
        this.bookLink = bookLink;
        this.bookImage = bookImage;
        this.summary = summary;
    }

    public void setDiaryId(DiaryEntity diary) {
        this.diary = diary;
    }

}
