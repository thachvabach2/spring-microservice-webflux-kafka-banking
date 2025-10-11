package vn.bachdao.accountservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import vn.bachdao.accountservice.domain.Account;

public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

}
