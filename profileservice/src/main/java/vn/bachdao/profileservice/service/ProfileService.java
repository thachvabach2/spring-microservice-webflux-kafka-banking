package vn.bachdao.profileservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.bachdao.profileservice.domain.dto.ProfileDTO;
import vn.bachdao.profileservice.repository.ProfileRepository;

@Service
@Slf4j
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Flux<ProfileDTO> getAllProfile() {
        return this.profileRepository.findAll()
                .map(profile -> ProfileDTO.entityToDto(profile))
                .switchIfEmpty(Mono.error(new Exception("Profile list empty")));
    }
}
