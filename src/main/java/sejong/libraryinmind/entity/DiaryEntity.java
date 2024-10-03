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
    @Column(name = "diary_id")
    private Long id;

    @Column(name = "diary_keyword_1",length = 10, nullable = false)
    private String keyword_1;

    @Column(name = "diary_keyword_2",length = 10, nullable = false)
    private String keyword_2;

    @Column(name = "diary_keyword_3",length = 10, nullable = false)
    private String keyword_3;

    @Column(name = "diary_fileId",nullable = false)
    private Long fileId; //일기 사진

    @CreatedDate
    @Column(name = "diary_date" , updatable = false)
    private LocalDateTime createdDate;

    @Builder
    public DiaryEntity(Long id, String keyword_1, String keyword_2, String keyword_3, Long fileId){
        this.id = id;
        this.keyword_1 = keyword_1;
        this.keyword_2 = keyword_2;
        this.keyword_3 = keyword_3;
        this.fileId = fileId;
    }


    //ToDo: 추천 도서 이미지, 소개 저장
}
