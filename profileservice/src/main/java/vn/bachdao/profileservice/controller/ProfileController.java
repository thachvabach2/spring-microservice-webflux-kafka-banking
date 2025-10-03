package vn.bachdao.profileservice.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.bachdao.commonservice.utils.CommonFunction;
import vn.bachdao.profileservice.domain.dto.ProfileDTO;
import vn.bachdao.profileservice.service.ProfileService;
import vn.bachdao.profileservice.utils.Constant;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<Flux<ProfileDTO>> getAllProfile() {
        return ResponseEntity.ok(this.profileService.getAllProfile());
    }

    @GetMapping("/checkDuplicate/{email}")
    public ResponseEntity<Mono<Boolean>> checkDuplicate(@PathVariable("email") String email) {
        return ResponseEntity.ok(this.profileService.checkDuplicate(email));
    }

    @PostMapping
    public ResponseEntity<Mono<ProfileDTO>> createNewProfile(@RequestBody String requestStr)
            throws JsonMappingException, JsonProcessingException {

        InputStream inputStream = ProfileController.class.getClassLoader()
                .getResourceAsStream(Constant.JSON_REQ_CREATE_PROFILE);

        CommonFunction.jsonValidate(inputStream, requestStr);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.profileService.createNewProfile(objectMapper.readValue(requestStr, ProfileDTO.class)));
    }
}
