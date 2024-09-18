package part1.example.aspire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import part1.example.aspire.model.Employee;
import part1.example.aspire.model.EmployeeResponseGet;
import part1.example.aspire.model.EmployeeResponseUpdate;
import part1.example.aspire.repository.EmployeeRepository;

@Service
public class AspireService {
    @Autowired
    private EmployeeRepository employeeRepository;
    public ResponseEntity<EmployeeResponseUpdate> addEmployee(Employee employee) {
        employeeRepository.save(employee);
        EmployeeResponseUpdate employeeResponseUpdate=new EmployeeResponseUpdate("Employee added successfully");
        return ResponseEntity.ok(employeeResponseUpdate);
    }
    public ResponseEntity<EmployeeResponseGet> getEmployee(String startletter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEmployee'");
    }

    
    
}
