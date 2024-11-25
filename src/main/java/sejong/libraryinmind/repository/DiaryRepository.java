package sejong.libraryinmind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.libraryinmind.entity.DiaryEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
    List<DiaryEntity> findByCreatedDateBetweenAndUserId(LocalDateTime startDate, LocalDateTime endDate, Long userId);

}
