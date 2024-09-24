package part1.example.aspire.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import part1.example.aspire.model.Employee;
import part1.example.aspire.model.EmployeeDetailsDTO;
import part1.example.aspire.model.EmployeeResponseGet;
import part1.example.aspire.model.EmployeeResponseUpdate;
import part1.example.aspire.model.Stream;
import part1.example.aspire.model.StreamResponseGet;
import part1.example.aspire.repository.EmployeeRepository;
import part1.example.aspire.repository.StreamRepository;


@Service
public class AspireService {
    @Autowired
    private EmployeeRepository employeeRepository;
    
    // @Autowired
    // private AccountRepository accountRepository;
    
    @Autowired
    private StreamRepository streamRepository;

    public ResponseEntity<EmployeeResponseGet> getEmployees(String startLetter) {

        List<Employee> employees = employeeRepository.findByEmpNameStartingWith(startLetter);

        List<EmployeeDetailsDTO> employeeDetails = new ArrayList<>();
    
        for (Employee employee : employees) {
            EmployeeDetailsDTO employeeDetailsDTO = new EmployeeDetailsDTO();
            employeeDetailsDTO.setEmpName(employee.getEmpName());
            employeeDetailsDTO.setEmpId(employee.getEmpId());
            employeeDetailsDTO.setDesignation(employee.getDesignation());
    
            if (employee.getAccount() != null) {
                employeeDetailsDTO.setAccountName(employee.getAccount().getAccountName());
            }
    
            if (employee.getStream() != null) {
                employeeDetailsDTO.setStreamName(employee.getStream().getStreamName());
            }
    
            employeeDetailsDTO.setManagerName(employee.getManager() != null 
                ? employee.getManager().getEmpName() 
                : "Self (Manager)"); 
    
            employeeDetails.add(employeeDetailsDTO);
        }
    
        String responseMessage = employeeDetails.isEmpty() ? "No employee found" : "Successfully fetched";
        EmployeeResponseGet employeeResponseGet = new EmployeeResponseGet(responseMessage, employeeDetails);
        
        return ResponseEntity.ok(employeeResponseGet);
    }


    public ResponseEntity<StreamResponseGet> getStream(){
        List<Stream> streams=streamRepository.findAll();
        
        Set<String> streamSet = new HashSet<>();
        for (Stream stream:streams){
            streamSet.add(stream.getStreamName());
        }
        String responseMessage=streams.isEmpty()? "No stream found" : "Successfully fetched";
        StreamResponseGet streamResponseGet=new StreamResponseGet(responseMessage,streamSet);
        return ResponseEntity.ok(streamResponseGet);
    }


    public ResponseEntity<EmployeeResponseUpdate> updateEmployee(Integer empId, Integer managerId) {
        Employee employee = employeeRepository.findById(empId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + empId));

        if (managerId==0) {
            employee.setDesignation("Manager");
        }
        else{
            employee.setDesignation("Associate");
        }

        if (managerId != null && managerId == 0) {
            employee.setManager(null); 
        } else if (managerId != null) {
            Employee manager = employeeRepository.findById(managerId)
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with ID: " + managerId));
            employee.setManager(manager);
            employee.setStream(manager.getStream());
            employee.setAccount(manager.getAccount());
          
        }

        employeeRepository.save(employee);
        String successMessage = employee.getEmpName() + "'s details have been successfully updated.";
        EmployeeResponseUpdate response = new EmployeeResponseUpdate(successMessage);
        
        return ResponseEntity.ok(response);
            
        }


    public ResponseEntity<EmployeeResponseUpdate> addEmployee(Employee employee) {
        return null;
    }


    
}


