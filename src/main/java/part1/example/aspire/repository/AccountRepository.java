package part1.example.aspire.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import part1.example.aspire.model.Account;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    
}
