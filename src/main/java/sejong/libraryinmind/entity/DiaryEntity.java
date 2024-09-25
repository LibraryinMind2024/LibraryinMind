package sejong.libraryinmind.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
//jpa에게 해당 엔티티는 Auditiong 기능을 사용함을 알림
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String keyword_1;

    @Column(length = 10, nullable = false)
    private String keyword_2;

    @Column(length = 10, nullable = false)
    private String keyword_3;

    @Column(nullable = false)
    private Long fileId; //일기 사진

    @Builder
    public DiaryEntity(Long id, String keyword_1, String keyword_2, String keyword_3, Long fileId){
        this.id = id;
        this.keyword_1 = keyword_1;
        this.keyword_2 = keyword_2;
        this.keyword_3 = keyword_3;
        this.fileId = fileId;
    }


    //ToDo: 날짜 저장, 추천 도서 이미지, 소개 저장
}
