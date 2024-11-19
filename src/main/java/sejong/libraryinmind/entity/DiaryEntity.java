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
    @Column(name = "content" , nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Builder
    public DiaryEntity(Long id, String content){
        this.id = id;
        this.content = content;
    }

    //    @Column(name = "file_id",nullable = false)
    //    private Long fileId; //일기 사진

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
