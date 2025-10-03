package vn.bachdao.profileservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import vn.bachdao.profileservice.domain.Profile;

@Repository
public interface ProfileRepository extends ReactiveCrudRepository<Profile, Long> {

    Mono<Profile> findByEmail(String email);
}
