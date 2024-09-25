package sejong.libraryinmind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.libraryinmind.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
