package part1.example.aspire.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import part1.example.aspire.model.Employee;
import part1.example.aspire.model.EmployeeDetailsDTO;
import part1.example.aspire.model.EmployeeRequestDTO;
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
    
        if(employeeDetails.isEmpty()){
            EmployeeResponseGet employeeResponseGet = new EmployeeResponseGet("No employee found", employeeDetails);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeResponseGet);
        }

        else{
            EmployeeResponseGet employeeResponseGet = new EmployeeResponseGet("Successfully fetched", employeeDetails);
            return ResponseEntity.ok(employeeResponseGet);
        }
        
    }


    public ResponseEntity<StreamResponseGet> getStream(){
        List<Stream> streams=streamRepository.findAll();
        
        Set<String> streamSet = new HashSet<>();
        for (Stream stream:streams){
            streamSet.add(stream.getStreamName());
        }

        if(streams.isEmpty()){
            StreamResponseGet streamResponseGet=new StreamResponseGet("No stream found" ,streamSet);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(streamResponseGet);
        }
        
        else{
            StreamResponseGet streamResponseGet=new StreamResponseGet("Successfully fetched" ,streamSet);
            return ResponseEntity.ok(streamResponseGet);
        }
    }


    // public ResponseEntity<EmployeeResponseUpdate> addEmployee(EmployeeRequestDTO employeeRequestDTO) {
    //     System.out.println(employeeRequestDTO.getManagerId());
    //     System.out.println(employeeRequestDTO.getStreamId());
    //     // if(!accountRepository.existsById(employeeRequestDTO.getAccountId())){
    //     //     EmployeeResponseUpdate employeeResponseUpdate=new EmployeeResponseUpdate("Account id not found");
    //     //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeResponseUpdate);
    //     // }

    //     // if(!streamRepository.existsById(employeeRequestDTO.getStreamId())){
    //     //     EmployeeResponseUpdate employeeResponseUpdate=new EmployeeResponseUpdate("Stream id not found");
    //     //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeResponseUpdate);
    //     // }

    //     Employee employee=new Employee();
    //     employee.setEmpName(employeeRequestDTO.getEmpName());

    //     if(employeeRequestDTO.getManagerId()!=null && employeeRequestDTO.getStreamId()==null){
    //         employee.setDesignation("Associate");

    //         Optional<Employee> manager=employeeRepository.findById(employeeRequestDTO.getManagerId());
    //         if(!manager.get().getDesignation().equals("Manager")){
    //             EmployeeResponseUpdate employeeResponseUpdate=new EmployeeResponseUpdate("The manager ID doesn't belongs to a manager");
    //             return ResponseEntity.badRequest().body(employeeResponseUpdate);
    //         }
    //         employee.setStream(manager.get().getStream());
    //         employee.setAccount(manager.get().getAccount());
    //         employee.setManager(manager.get());
    //     }
       
    //     else if(employeeRequestDTO.getManagerId()==null && employeeRequestDTO.getStreamId()!=null){
    //         if(!streamRepository.existsById(employeeRequestDTO.getStreamId())){
    //             EmployeeResponseUpdate employeeResponseUpdate=new EmployeeResponseUpdate("Stream id not found");
    //             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employeeResponseUpdate);
    //         }
    //         employee.setDesignation("Manager");
    //         Optional<Employee> existingManager = employeeRepository.findByStream_StreamIdAndDesignation(employeeRequestDTO.getStreamId(),employee.getDesignation());
    //         if (existingManager.isPresent()) {
    //             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                     .body(new EmployeeResponseUpdate("Manager already exists for this stream"));
    //         }
    //         Optional<Stream> streamOpt=streamRepository.findById(employeeRequestDTO.getStreamId());

    //         employee.setStream(streamOpt.get());
    //         employee.setAccount(streamOpt.get().getAccount());
    //         employee.setManager(null);
    //     }

    //     else {
    //         EmployeeResponseUpdate responseUpdate = new EmployeeResponseUpdate("Provide streamId to add manager and provide managerId to add employee. Don't provide both");
    //         return ResponseEntity.badRequest().body(responseUpdate);
    //     }
    //     employeeRepository.save(employee);
    //     EmployeeResponseUpdate responseUpdate = new EmployeeResponseUpdate("Employee added successfully");
    //     return ResponseEntity.ok(responseUpdate);
        
    // }

    public ResponseEntity<EmployeeResponseUpdate> addEmployee(EmployeeRequestDTO employeeRequestDTO) {
        System.out.println(employeeRequestDTO.getManagerId());
        System.out.println(employeeRequestDTO.getStreamId());
        // Check for both IDs
        if (employeeRequestDTO.getManagerId() != null && employeeRequestDTO.getStreamId() != null) {
            return ResponseEntity.badRequest()
                    .body(new EmployeeResponseUpdate("Provide streamId to add manager and provide managerId to add employee. Don't provide both"));
        }
    
        Employee employee = new Employee();
        employee.setEmpName(employeeRequestDTO.getEmpName());
    
        if (employeeRequestDTO.getManagerId() != null) {
            return handleEmployeeUnderManager(employeeRequestDTO, employee);
        } else if (employeeRequestDTO.getStreamId() != null) {
            return handleNewManager(employeeRequestDTO, employee);
        }
    
        return ResponseEntity.badRequest().body(new EmployeeResponseUpdate("No valid manager or stream ID provided."));
    }
    
    private ResponseEntity<EmployeeResponseUpdate> handleEmployeeUnderManager(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        employee.setDesignation("Associate");
    
        Employee manager = employeeRepository.findById(employeeRequestDTO.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager not found"));
    
        if (!"Manager".equals(manager.getDesignation())) {
            return ResponseEntity.badRequest()
                    .body(new EmployeeResponseUpdate("The manager ID doesn't belong to a manager"));
        }
    
        employee.setStream(manager.getStream());
        employee.setAccount(manager.getAccount());
        employee.setManager(manager);
    
        employeeRepository.save(employee);
        return ResponseEntity.ok(new EmployeeResponseUpdate("Employee added successfully"));
    }
    
    private ResponseEntity<EmployeeResponseUpdate> handleNewManager(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        if (!streamRepository.existsById(employeeRequestDTO.getStreamId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new EmployeeResponseUpdate("Stream ID not found"));
        }
    
        employee.setDesignation("Manager");
        Optional<Employee> existingManager = employeeRepository.findByStream_StreamIdAndDesignation(employeeRequestDTO.getStreamId(), "Manager");
        
        if (existingManager.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EmployeeResponseUpdate("Manager already exists for this stream"));
        }
    
        Stream stream = streamRepository.findById(employeeRequestDTO.getStreamId())
                .orElseThrow(() -> new EntityNotFoundException("Stream not found"));
    
        employee.setStream(stream);
        employee.setAccount(stream.getAccount());
        employee.setManager(null);
    
        employeeRepository.save(employee);
        return ResponseEntity.ok(new EmployeeResponseUpdate("Manager added successfully"));
    }
    

    public ResponseEntity<EmployeeResponseUpdate> updateEmployeeToManager(Integer empId, Integer streamId) {
        Optional<Stream> optionalStream = streamRepository.findById(streamId);
        String designation="Manager";
        if (optionalStream.isPresent()) {
            Stream stream = optionalStream.get();
           
            Optional<Employee> existingManager = employeeRepository.findByStream_StreamIdAndDesignation(streamId,designation);
            if (existingManager.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new EmployeeResponseUpdate("Manager already exists for this stream"));
            }
    
            Optional<Employee> optionalEmployee = employeeRepository.findById(empId);
            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();
    
                if ("manager".equalsIgnoreCase(designation)) {
                    List<Employee> subordinates = employeeRepository.findByManager(employee);
                    if (!subordinates.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new EmployeeResponseUpdate("The employee ID belongs to a manager with subemployees"));
                    }
                }
                employee.setManager(null);
                employee.setDesignation(designation);
                employee.setStream(stream);
                employee.setAccount(stream.getAccount());
        
                employeeRepository.save(employee);
                EmployeeResponseUpdate response = new EmployeeResponseUpdate(employee.getEmpName() + "'s details have been successfully updated.");
                return ResponseEntity.ok(response);
            } 
            
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new EmployeeResponseUpdate("Employee not found"));
            }
        } 
        
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new EmployeeResponseUpdate("Stream not found"));
        }
    }


    public ResponseEntity<EmployeeResponseUpdate> updateManagerToEmployee(Integer empId, Integer managerId) {
        String designation="Associate";
        Employee employee = employeeRepository.findById(empId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + empId));
        
        if (managerId != null ) {
            Employee manager = employeeRepository.findById(managerId)
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with ID: " + managerId));
            
            List<Employee> subordinates = employeeRepository.findByManager(employee);

            if (!subordinates.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new EmployeeResponseUpdate("The employee ID belongs to a manager with subemployees"));
            }

            if(manager.getManager()!=null){
                EmployeeResponseUpdate employeeResponseUpdate=new EmployeeResponseUpdate("The manager ID doesn't belongs to a manager");
                return ResponseEntity.badRequest().body(employeeResponseUpdate);
            }

            employee.setManager(manager);
            employee.setDesignation(designation);
            employee.setStream(manager.getStream());
            employee.setAccount(manager.getAccount());
        } 
           
        employeeRepository.save(employee);
        String successMessage = employee.getEmpName() + "'s details have been successfully updated.";
        EmployeeResponseUpdate response = new EmployeeResponseUpdate(successMessage);
        return ResponseEntity.ok(response); 

    }
}


