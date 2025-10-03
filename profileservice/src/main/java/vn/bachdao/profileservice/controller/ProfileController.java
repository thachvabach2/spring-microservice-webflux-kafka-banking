package vn.bachdao.profileservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.bachdao.profileservice.domain.dto.ProfileDTO;
import vn.bachdao.profileservice.service.ProfileService;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<Flux<ProfileDTO>> getAllProfile() {
        return ResponseEntity.ok(this.profileService.getAllProfile());
    }

    @GetMapping("/checkDuplicate/{email}")
    public ResponseEntity<Mono<Boolean>> checkDuplicate(@PathVariable("email") String email) {
        return ResponseEntity.ok(this.profileService.checkDuplicate(email));
    }

    @PostMapping
    public ResponseEntity<Mono<ProfileDTO>> createNewProfile(@RequestBody ProfileDTO profileDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.profileService.createNewProfile(profileDTO));
    }
}
