package esprit.tn.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import esprit.tn.shared.config.DTO.MatchScoreUpdateEvent;
import esprit.tn.shared.config.DTO.ScheduleGeneratedEvent;
import esprit.tn.shared.config.DTO.TournamentCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration des KafkaListenerContainerFactory avec désérialisation typée.
 */
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ObjectMapper kafkaObjectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    private <T> JsonDeserializer<EventEnvelop<T>> typedDeserializer(Class<T> payloadType, ObjectMapper objectMapper) {
        var javaType = objectMapper.getTypeFactory()
                .constructParametricType(EventEnvelop.class, payloadType);
        JsonDeserializer<EventEnvelop<T>> deserializer = new JsonDeserializer<>(javaType, objectMapper);
        deserializer.addTrustedPackages("*");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(false);
        return deserializer;
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, EventEnvelop<T>> buildFactory(Class<T> payloadType, ObjectMapper objectMapper) {
        var deserializer = typedDeserializer(payloadType, objectMapper);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "standings-service");

        var consumerFactory = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EventEnvelop<T>>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean(name = "matchScoreKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, EventEnvelop<MatchScoreUpdateEvent>> matchScoreKafkaFactory(ObjectMapper kafkaObjectMapper) {
        return buildFactory(MatchScoreUpdateEvent.class, kafkaObjectMapper);
    }

    @Bean(name = "scheduleGeneratedKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, EventEnvelop<ScheduleGeneratedEvent>> scheduleGeneratedKafkaFactory(ObjectMapper kafkaObjectMapper) {
        return buildFactory(ScheduleGeneratedEvent.class, kafkaObjectMapper);
    }

    @Bean(name = "tournamentCreatedKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, EventEnvelop<TournamentCreatedEvent>> tournamentCreatedKafkaFactory(ObjectMapper kafkaObjectMapper) {
        return buildFactory(TournamentCreatedEvent.class, kafkaObjectMapper);
    }
}
