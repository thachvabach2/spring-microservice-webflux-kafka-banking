package vn.bachdao.accountservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Account {

    @Id
    private String id;

    private String email;
    private String currency;
    private double balance;
    private double reserved;
}
