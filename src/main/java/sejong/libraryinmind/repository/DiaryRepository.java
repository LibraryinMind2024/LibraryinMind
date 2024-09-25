package sejong.libraryinmind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.libraryinmind.entity.DiaryEntity;

public interface DiaryRepository extends JpaRepository<DiaryEntity, Long> {
}
