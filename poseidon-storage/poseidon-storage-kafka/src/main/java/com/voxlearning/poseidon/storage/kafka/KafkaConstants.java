

package com.voxlearning.poseidon.storage.kafka;

public class KafkaConstants {

    public static final String PROPERTY_PREFIX = "kafka.";

    /* Properties */

    public static final String CONFIG_PATH = "kafka.properties";


    public static final String TOPIC = "topic";
    public static final String BATCH_SIZE = "batchSize";
    public static final String MESSAGE_SERIALIZER_KEY = "serializer.class";
    public static final String KEY_SERIALIZER_KEY = "key.serializer.class";
    public static final String BROKER_LIST_KEY = "metadata.broker.list";
    public static final String REQUIRED_ACKS_KEY = "request.required.acks";
    public static final String BROKER_LIST_FLUME_KEY = "brokerList";
    public static final String REQUIRED_ACKS_FLUME_KEY = "requiredAcks";

    public static final int DEFAULT_BATCH_SIZE = 100;
    public static final String DEFAULT_TOPIC = "default-flume-topic";
    public static final String DEFAULT_MESSAGE_SERIALIZER =
            "kafka.serializer.DefaultEncoder";

    public static final String DEFAULT_KEY_SERIALIZER =
            "org.apache.kafka.common.serialization.StringSerializer";
    public static final String DEFAULT_VALUE_SERIAIZER =
            "org.apache.kafka.common.serialization.ByteArraySerializer";


    public static final String DEFAULT_REQUIRED_ACKS = "1";

    public static final String DEFAULT_ACKS = "1";
}
