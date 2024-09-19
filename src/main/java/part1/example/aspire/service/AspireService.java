package part1.example.aspire.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

        List<String> streamList=new ArrayList<>();
        for (Stream stream:streams){
            streamList.add(stream.getStreamName());
        }
        String responseMessage=streams.isEmpty()? "No stream found" : "Successfully fetched";
        StreamResponseGet streamResponseGet=new StreamResponseGet(responseMessage,streamList);
        return ResponseEntity.ok(streamResponseGet);
    }


    public ResponseEntity<EmployeeResponseUpdate> updateEmployee(Integer empId, Integer managerId) {
        return null;
    }


    
}


