package sejong.libraryinmind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.libraryinmind.entity.BookEntity;
import sejong.libraryinmind.entity.DiaryEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    BookEntity findByDiary(DiaryEntity diary);
}

