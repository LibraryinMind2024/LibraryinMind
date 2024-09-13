package sejong.libraryinmind.entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer")

public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customer_id;

    @Column(name = "customer_username", nullable = false)
    private String customer_username;

    @Column(name = "customer_password", nullable = false)
    private String customer_password;
}
