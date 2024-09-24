package part1.example.aspire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import part1.example.aspire.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
    List<Employee> findByEmpNameStartingWith(String name);

    
}
