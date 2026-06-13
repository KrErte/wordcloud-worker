package ee.bitweb.wordcloud_worker.messaging;

import java.util.UUID;

public record TextChunkMessage(
        UUID jobId,
        String filename,
        String content
) {
}