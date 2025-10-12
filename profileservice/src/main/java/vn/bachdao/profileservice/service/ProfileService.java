package vn.bachdao.profileservice.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.bachdao.profileservice.domain.dto.ProfileDTO;
import vn.bachdao.profileservice.event.EventProducer;
import vn.bachdao.profileservice.repository.ProfileRepository;
import vn.bachdao.commonservice.utils.Constant;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Flux<ProfileDTO> getAllProfile() {
        return this.profileRepository.findAll()
                .map(ProfileDTO::entityToDto)
                .switchIfEmpty(Mono.error(new Exception("Profile list empty")));
    }

    public Mono<Boolean> checkDuplicate(String email) {
        return this.profileRepository.findByEmail(email)
                .flatMap(profile -> Mono.just(true))
                .switchIfEmpty(Mono.just(false));
    }

    public Mono<ProfileDTO> createNewProfile(ProfileDTO profileDTO) {
        return this.checkDuplicate(profileDTO.getEmail())
                .flatMap(aBoolean -> {
                    if (Boolean.TRUE.equals(aBoolean)) {
                        return Mono.error(new Exception("Duplicate profile"));
                    } else {
                        profileDTO.setStatus(Constant.STATUS_PROFILE_PENDING);
                        return createProfile(profileDTO);
                    }
                });
    }

    public Mono<ProfileDTO> createProfile(ProfileDTO profileDTO) {
        return Mono.just(profileDTO)
                .map(ProfileDTO::dtoToEntity)
                .flatMap(profile -> this.profileRepository.save(profile))
                .map(ProfileDTO::entityToDto)
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .doOnSuccess(dto -> {
                    if (Objects.equals(dto.getStatus(), Constant.STATUS_PROFILE_PENDING)) {
                        dto.setInitialBalance(profileDTO.getInitialBalance());
                        try {
                            this.eventProducer.send(Constant.PROFILE_ONBOARDING_TOPIC,
                                    objectMapper.writeValueAsString(profileDTO)).subscribe();
                        } catch (JsonProcessingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }
}
