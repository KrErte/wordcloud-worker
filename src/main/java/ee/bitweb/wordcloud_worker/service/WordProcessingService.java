package ee.bitweb.wordcloud_worker.service;

import ee.bitweb.wordcloud_worker.entity.JobStatus;
import ee.bitweb.wordcloud_worker.entity.TextJob;
import ee.bitweb.wordcloud_worker.entity.WordCount;
import ee.bitweb.wordcloud_worker.repository.TextJobRepository;
import ee.bitweb.wordcloud_worker.repository.WordCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordProcessingService {

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "but", "in", "on", "at",
            "to", "for", "of", "with", "by", "from", "is", "it",
            "as", "be", "was", "are", "were", "that", "this", "he",
            "she", "they", "we", "you", "i", "have", "has", "had",
            "do", "did", "not", "so", "if", "its", "than", "then"
    );

    private final TextJobRepository textJobRepository;
    private final WordCountRepository wordCountRepository;

    @Transactional
    public void process(UUID jobId, String content) {
        TextJob job = textJobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        job.setStatus(JobStatus.IN_PROGRESS);
        textJobRepository.save(job);

        Map<String, Long> wordCounts = Arrays.stream(content.toLowerCase().split("[^a-zA-Z]+"))
                .filter(w -> !w.isBlank())
                .filter(w -> w.length() > 1)
                .filter(w -> !STOP_WORDS.contains(w))
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        List<WordCount> entities = wordCounts.entrySet().stream()
                .map(e -> {
                    WordCount wc = new WordCount();
                    wc.setJobId(jobId);
                    wc.setWord(e.getKey());
                    wc.setCount(e.getValue().intValue());
                    return wc;
                })
                .toList();

        wordCountRepository.saveAll(entities);

        job.setStatus(JobStatus.COMPLETE);
        textJobRepository.save(job);

        log.info("Job {} complete — {} unique words", jobId, entities.size());
    }
}