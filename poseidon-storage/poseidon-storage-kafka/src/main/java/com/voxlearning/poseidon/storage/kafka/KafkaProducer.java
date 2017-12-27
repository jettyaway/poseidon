package com.voxlearning.poseidon.storage.kafka;

import com.voxlearning.poseidon.settings.dialect.Props;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Objects;
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
        for (byte[] message : messages) {
            record = new ProducerRecord<>(topic, null,
                    message);
            if (Objects.isNull(callback)) {
                kafkaFutures.add(producer.send(record));
            } else {
                kafkaFutures.add(producer.send(record, callback));
            }
        }
        producer.flush();
    }
}


