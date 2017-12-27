package com.voxlearning.poseidon.storage.kafka;

import com.voxlearning.poseidon.core.lang.Preconditions;
import com.voxlearning.poseidon.core.util.CharsetUtil;
import com.voxlearning.poseidon.settings.dialect.Props;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.voxlearning.poseidon.storage.kafka.KafkaConstants.DEFAULT_ACKS;
import static com.voxlearning.poseidon.storage.kafka.KafkaConstants.DEFAULT_KEY_SERIALIZER;
import static com.voxlearning.poseidon.storage.kafka.KafkaConstants.DEFAULT_VALUE_SERIAIZER;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-27
 * @since 17-12-27
 */
public abstract class AbstractProducer implements IProducer {

    Producer<String, byte[]> producer;

    List<Future<RecordMetadata>> kafkaFutures;

    private Props properties;

    public AbstractProducer() {
        this(KafkaConstants.CONFIG_PATH);
    }

    public AbstractProducer(String propsPath) {
        this(new Props(propsPath));
    }

    public AbstractProducer(Properties properties) {
        Preconditions.checkNotNull(properties);
        this.properties = new Props(properties);
        kafkaFutures = new LinkedList<>();
        start();
    }

    @Override
    public void start() {
        initConfig();
        producer = new KafkaProducer<>(properties);
    }

    private void initConfig() {
        //overwrite config
        properties.put(ProducerConfig.ACKS_CONFIG, DEFAULT_ACKS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, DEFAULT_KEY_SERIALIZER);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DEFAULT_VALUE_SERIAIZER);
    }

    @Override
    public void stop() {
        producer.close();
    }

    @Override
    public void send(String topic, List<String> messages,Callback callback) {
        List<byte[]> collect = messages.parallelStream().map(message -> message.getBytes(CharsetUtil.CHARSET_UTF_8))
                .collect(Collectors.toList());
        sendBytes(topic, collect,callback);
    }

    @Override
    public void sendBytes(String topic, List<byte[]> message) {
        sendBytes(topic, message, null);
    }

    @Override
    public void sendBytes(String topic, List<byte[]> message, Callback callback) {
        kafkaFutures.clear();
        doSend(topic, message, callback);
    }

    abstract void doSend(String tipic, List<byte[]> message, Callback callback);
}
