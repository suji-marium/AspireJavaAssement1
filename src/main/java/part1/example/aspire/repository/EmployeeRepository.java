package part1.example.aspire.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import part1.example.aspire.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
    List<Employee> findByEmpNameStartingWith(String name);
    Optional<Employee> findByStream_StreamIdAndDesignation(Integer streamId, String designation);
    List<Employee> findByManager(Employee manager);
}
