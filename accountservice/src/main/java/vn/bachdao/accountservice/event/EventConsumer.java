package vn.bachdao.accountservice.event;

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
import vn.bachdao.accountservice.domain.dto.AccountDTO;
import vn.bachdao.accountservice.domain.dto.ProfileDTO;
import vn.bachdao.accountservice.service.AccountService;
import vn.bachdao.commonservice.utils.Constant;

@Service
@Slf4j
public class EventConsumer {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AccountService accountService;

    public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
        KafkaReceiver.create(receiverOptions.subscription(Collections.singleton(Constant.PROFILE_ONBOARDING_TOPIC)))
                .receive().subscribe(t -> {
                    try {
                        this.profileOnboarding(t);
                    } catch (JsonProcessingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
    }

    public void profileOnboarding(ReceiverRecord<String, String> receiverRecord)
            throws JsonMappingException, JsonProcessingException {
        log.info("Profile Onboarding event");

        ProfileDTO dto = this.objectMapper.readValue(receiverRecord.value(), ProfileDTO.class);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(dto.getEmail());
        accountDTO.setReserved(0);
        accountDTO.setBalance(dto.getInitialBalance());
        accountDTO.setCurrency("USD");

        this.accountService.createNewAccount(accountDTO)
                .subscribe();
    }
}
