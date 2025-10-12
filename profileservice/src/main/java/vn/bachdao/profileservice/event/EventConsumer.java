package vn.bachdao.profileservice.event;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import vn.bachdao.commonservice.utils.Constant;
import vn.bachdao.profileservice.domain.dto.ProfileDTO;
import vn.bachdao.profileservice.service.ProfileService;

@Service
@Slf4j
public class EventConsumer {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProfileService profileService;

    public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
        KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PROFILE_ONBOARDED_TOPIC)))
                .receive().subscribe(t -> {
                    try {
                        profileOnboarded(t);
                    } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
    }

    public void profileOnboarded(ReceiverRecord<String, String> receiverRecord)
            throws JsonMappingException, JsonProcessingException {
        ProfileDTO dto = this.objectMapper.readValue(receiverRecord.value(), ProfileDTO.class);
        this.profileService.updateStatusProfile(dto).subscribe();
    }
}
