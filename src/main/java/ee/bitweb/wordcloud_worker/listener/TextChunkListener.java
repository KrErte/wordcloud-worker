package ee.bitweb.wordcloud_worker.listener;

import ee.bitweb.wordcloud_worker.messaging.TextChunkMessage;
import ee.bitweb.wordcloud_worker.service.WordProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextChunkListener {

    private final WordProcessingService wordProcessingService;

    @RabbitListener(queues = "${wordcloud.rabbitmq.queue}")
    public void onMessage(TextChunkMessage message) {
        log.info("Received job {}", message.jobId());
        wordProcessingService.process(message.jobId(), message.content());
    }
}