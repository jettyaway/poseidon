package com.voxlearning.poseidon.storage.kafka;

import com.voxlearning.poseidon.settings.dialect.Props;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-27
 * @since 17-12-27
 */
public class KafkaProducer extends AbstractProducer {


    public KafkaProducer() {
        super();
    }

    public KafkaProducer(Props props) {
        super(props);
    }

    public KafkaProducer(Properties properties) {
        super(properties);
    }


    @Override
    public void doSend(String topic, List<byte[]> messages, Callback callback) {
        ProducerRecord<String, byte[]> record;
        long startTime = System.currentTimeMillis();
        callback = (callback == null) ? new SinkCallback(startTime) : callback;
        for (byte[] message : messages) {
            record = new ProducerRecord<>(topic, null,
                    message);
            kafkaFutures.add(producer.send(record, callback));
        }
        producer.flush();
    }
}

class SinkCallback implements Callback {
    private static final Logger logger = LoggerFactory.getLogger(SinkCallback.class);
    private long startTime;

    public SinkCallback(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            logger.debug("Error sending message to Kafka {} ", exception.getMessage());
        }

        if (logger.isDebugEnabled()) {
            long eventElapsedTime = System.currentTimeMillis() - startTime;
            logger.debug("Acked message partition:{} ofset:{}", metadata.partition(), metadata.offset());
            logger.debug("Elapsed time for send: {}", eventElapsedTime);
        }
    }
}
