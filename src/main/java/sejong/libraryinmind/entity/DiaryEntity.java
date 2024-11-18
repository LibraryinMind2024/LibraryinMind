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

    @Column(name = "file_id",nullable = false)
    private Long fileId; //일기 사진

    @CreatedDate
    @Column(name = "date" , updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Builder
    public DiaryEntity(Long id, Long fileId){
        this.id = id;
        this.fileId = fileId;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
}
