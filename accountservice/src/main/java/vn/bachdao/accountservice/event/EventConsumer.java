package vn.bachdao.accountservice.event;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import vn.bachdao.commonservice.utils.Constant;

@Service
@Slf4j
public class EventConsumer {

    ObjectMapper objectMapper = new ObjectMapper();

    public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
        KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PROFILE_ONBOARDING_TOPIC)))
                .receive().subscribe(this::profileOnboarding);
    }

    public void profileOnboarding(ReceiverRecord<String, String> receiverRecord) {
        log.info(receiverRecord.value());
    }
}
