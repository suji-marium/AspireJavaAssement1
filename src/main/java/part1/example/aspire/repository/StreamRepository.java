package part1.example.aspire.repository;

import part1.example.aspire.model.Stream;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamRepository extends JpaRepository<Stream,Integer>{
    
}
