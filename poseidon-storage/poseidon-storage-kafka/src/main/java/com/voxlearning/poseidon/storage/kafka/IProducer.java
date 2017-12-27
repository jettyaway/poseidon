package com.voxlearning.poseidon.storage.kafka;

import org.apache.kafka.clients.producer.Callback;

import java.util.List;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-27
 * @since 17-12-27
 */
public interface IProducer {

    void start();

    void stop();


    void send(String topic, List<String> message);

    void sendBytes(String topic, List<byte[]> message);

    void sendBytes(String topic, List<byte[]> message, Callback Callback);
}
