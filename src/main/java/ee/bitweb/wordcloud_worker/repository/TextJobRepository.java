package ee.bitweb.wordcloud_worker.repository;

import ee.bitweb.wordcloud_worker.entity.TextJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TextJobRepository extends JpaRepository<TextJob, UUID> {
}