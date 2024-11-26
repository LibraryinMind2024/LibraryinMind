package sejong.libraryinmind.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "diary")

//jpa에게 해당 엔티티는 Auditiong 기능을 사용함을 알림
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "date" , updatable = false)
    private LocalDateTime createdDate;

    @Lob
    @Column(name = "content" , nullable = false, length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToOne(mappedBy = "diary", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private BookEntity book;

    @Builder
    public DiaryEntity(Long id, String content, String imageUrl){
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
    }
    @Override
    public String toString() {
        return "DiaryEntity{id=" + id + ", createdDate=" + createdDate +  "imageUrl"+ imageUrl + ", content='" + content + "'}";
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

}
