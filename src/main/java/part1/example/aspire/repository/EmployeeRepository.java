package part1.example.aspire.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import part1.example.aspire.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
    
}
