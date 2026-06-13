package ee.bitweb.wordcloud_worker.repository;

import ee.bitweb.wordcloud_worker.entity.WordCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordCountRepository extends JpaRepository<WordCount, Long> {
}