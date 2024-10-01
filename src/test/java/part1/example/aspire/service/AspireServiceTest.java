package part1.example.aspire.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityNotFoundException;
import part1.example.aspire.model.*;
import part1.example.aspire.repository.EmployeeRepository;
import part1.example.aspire.repository.StreamRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AspireServiceTest {

    @InjectMocks
    private AspireService aspireService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private StreamRepository streamRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEmployee_WithBothManagerIdAndStreamId() {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setManagerId(1);
        requestDTO.setStreamId(2);

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.addEmployee(requestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Provide streamId to add manager and provide managerId to add employee. Don't provide both", response.getBody().getMessage());
    }

    @Test
    public void testAddEmployee_WithValidManagerId() {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setManagerId(1);

        Employee manager = new Employee();
        manager.setDesignation("Manager");
        when(employeeRepository.findById(1)).thenReturn(Optional.of(manager));

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.addEmployee(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee added successfully", response.getBody().getMessage());
        verify(employeeRepository).save(any(Employee.class)); 
    }

    @Test
    public void testAddEmployee_WithInvalidManagerId() {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setManagerId(1);

        Employee manager = new Employee();
        manager.setDesignation("Associate"); // Not a manager
        when(employeeRepository.findById(1)).thenReturn(Optional.of(manager));

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.addEmployee(requestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The manager ID doesn't belong to a manager", response.getBody().getMessage());
    }

    @Test
    public void testAddEmployee_WithValidStreamId() {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setStreamId(2);

        Stream stream = new Stream();
        when(streamRepository.existsById(2)).thenReturn(true);
        when(streamRepository.findById(2)).thenReturn(Optional.of(stream));
        when(employeeRepository.findByStream_StreamIdAndDesignation(2, "Manager")).thenReturn(Optional.empty());

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.addEmployee(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Manager added successfully", response.getBody().getMessage());
        verify(employeeRepository).save(any(Employee.class)); // Verify employee was saved
    }

    @Test
    public void testAddEmployee_StreamIdNotFound() {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setStreamId(2);

        when(streamRepository.existsById(2)).thenReturn(false);

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.addEmployee(requestDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Stream ID not found", response.getBody().getMessage());
    }

    @Test
    public void testAddEmployee_ManagerAlreadyExists() {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setStreamId(2);

        Stream stream = new Stream();
        when(streamRepository.existsById(2)).thenReturn(true);
        when(streamRepository.findById(2)).thenReturn(Optional.of(stream));
        when(employeeRepository.findByStream_StreamIdAndDesignation(2, "Manager"))
                .thenReturn(Optional.of(new Employee())); 

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.addEmployee(requestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Manager already exists for this stream", response.getBody().getMessage());
    }


    @Test
    void testGetEmployees_EmptyList() {
        String startLetter = "A";
        when(employeeRepository.findByEmpNameStartingWith(startLetter)).thenReturn(Collections.emptyList());

        ResponseEntity<EmployeeResponseGet> response = aspireService.getEmployees(startLetter);

        assertEquals("No employee found", response.getBody().getMessage());
        assertTrue(response.getBody().getEmployeeDetails().isEmpty());
    }

    @Test
    void testGetEmployees_Success() {
        String startLetter = "A";
        Employee employee = new Employee();
        employee.setEmpId(1);
        employee.setEmpName("Alice");
        employee.setDesignation("Manager");
        when(employeeRepository.findByEmpNameStartingWith(startLetter)).thenReturn(Collections.singletonList(employee));

        ResponseEntity<EmployeeResponseGet> response = aspireService.getEmployees(startLetter);

        assertEquals("Successfully fetched", response.getBody().getMessage());
        assertEquals(1, response.getBody().getEmployeeDetails().size());
        assertEquals("Alice", response.getBody().getEmployeeDetails().get(0).getEmpName());
    }

    @Test
    void testGetStream_EmptyList() {
        when(streamRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<StreamResponseGet> response = aspireService.getStream();

        assertEquals("No stream found", response.getBody().getMessage());
        assertTrue(response.getBody().getStreams().isEmpty());
    }

    @Test
    void testGetStream_Success() {
        Stream stream = new Stream();
        stream.setStreamName("Sales");
        when(streamRepository.findAll()).thenReturn(Collections.singletonList(stream));

        ResponseEntity<StreamResponseGet> response = aspireService.getStream();

        assertEquals("Successfully fetched", response.getBody().getMessage());
        assertEquals(1, response.getBody().getStreams().size());
        assertEquals("Sales", response.getBody().getStreams().iterator().next());
    }

    @Test
    void testUpdateEmployeeToManager_Success() {
        Integer empId = 1;
        Integer streamId = 2;
        String empName="Ramu";
        
        Stream stream = new Stream();
        stream.setStreamId(streamId);
        stream.setAccount(new Account()); // Assume Account exists

        Employee employee = new Employee();
        employee.setEmpName(empName);
        employee.setEmpId(empId);
        employee.setDesignation("Associate"); // Not a manager initially

        when(streamRepository.findById(streamId)).thenReturn(Optional.of(stream));
        when(employeeRepository.findByStream_StreamIdAndDesignation(streamId, "Manager")).thenReturn(Optional.empty());
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByManager(employee)).thenReturn(Collections.emptyList()); // No subordinates

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.updateEmployeeToManager(empId, streamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee.getEmpName() + "'s details have been successfully updated.", response.getBody().getMessage());
        assertEquals("Manager", employee.getDesignation());
        assertEquals(stream, employee.getStream());
        assertNull(employee.getManager()); // Manager should be set to null
    }

    @Test
    void testUpdateEmployeeToManager_EmployeeNotFound() {
        Integer empId = 1;
        Integer streamId = 2;

        when(streamRepository.findById(streamId)).thenReturn(Optional.of(new Stream()));
        when(employeeRepository.findById(empId)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.updateEmployeeToManager(empId, streamId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found", response.getBody().getMessage());
    }

    @Test
    void testUpdateEmployeeToManager_StreamNotFound() {
        Integer empId = 1;
        Integer streamId = 2;

        when(streamRepository.findById(streamId)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.updateEmployeeToManager(empId, streamId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Stream not found", response.getBody().getMessage());
    }

    @Test
    void testUpdateEmployeeToManager_ManagerAlreadyExists() {
        Integer empId = 1;
        Integer streamId = 2;

        Stream stream = new Stream();
        stream.setStreamId(streamId);
        stream.setAccount(new Account()); // Assume Account exists

        Employee existingManager = new Employee();
        existingManager.setEmpId(3); // Some existing manager

        Employee employee = new Employee();
        employee.setEmpId(empId);

        when(streamRepository.findById(streamId)).thenReturn(Optional.of(stream));
        when(employeeRepository.findByStream_StreamIdAndDesignation(streamId, "Manager")).thenReturn(Optional.of(existingManager));
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.updateEmployeeToManager(empId, streamId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Manager already exists for this stream", response.getBody().getMessage());
    }

    @Test
    void testUpdateEmployeeToManager_EmployeeHasSubordinates() {
        Integer empId = 1;
        Integer streamId = 2;

        Stream stream = new Stream();
        stream.setStreamId(streamId);
        stream.setAccount(new Account()); // Assume Account exists

        Employee employee = new Employee();
        employee.setEmpId(empId);
        employee.setDesignation("Manager"); // Already a manager

        Employee subordinate = new Employee(); // Subordinate
        subordinate.setEmpId(4);
        
        when(streamRepository.findById(streamId)).thenReturn(Optional.of(stream));
        when(employeeRepository.findByStream_StreamIdAndDesignation(streamId, "Manager")).thenReturn(Optional.empty());
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByManager(employee)).thenReturn(List.of(subordinate)); // Employee has subordinates

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.updateEmployeeToManager(empId, streamId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The employee ID belongs to a manager with subemployees", response.getBody().getMessage());
    }

    @Test
    void testUpdateManagerToEmployee_Success() {
        Integer empId = 1;
        Integer managerId = 2;
        
        Employee employee = new Employee();
        employee.setEmpId(empId);
        employee.setEmpName("John");
        employee.setDesignation("Manager");
        
        Employee manager = new Employee();
        manager.setEmpId(managerId);
        manager.setManager(null); 
        
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(employeeRepository.findByManager(employee)).thenReturn(Collections.emptyList());

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.updateManagerToEmployee(empId, managerId);

        assertEquals("John's details have been successfully updated.", response.getBody().getMessage());
        assertEquals("Associate", employee.getDesignation());
        assertEquals(manager, employee.getManager());
    }

    @Test
    void testUpdateManagerToEmployee_EmployeeNotFound() {
        Integer empId = 1;
        Integer managerId = 2;

        when(employeeRepository.findById(empId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            aspireService.updateManagerToEmployee(empId, managerId);
        });

        assertEquals("Employee not found with ID: " + empId, exception.getMessage());
    }

    @Test
    void testUpdateManagerToEmployee_ManagerNotFound() {
        Integer empId = 1;
        Integer managerId = 2;

        Employee employee = new Employee();
        employee.setEmpId(empId);

        when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(managerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            aspireService.updateManagerToEmployee(empId, managerId);
        });

        assertEquals("Manager not found with ID: " + managerId, exception.getMessage());
    }

    @Test
    void testUpdateManagerToEmployee_EmployeeHasSubordinates() {
        Integer empId = 1;
        Integer managerId = 2;

        Employee employee = new Employee();
        employee.setEmpId(empId);
        employee.setDesignation("Manager");

        Employee manager = new Employee();
        manager.setEmpId(managerId);
        manager.setManager(null); // Valid manager

        when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(employeeRepository.findByManager(employee)).thenReturn(List.of(new Employee())); // Employee has subordinates

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.updateManagerToEmployee(empId, managerId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The employee ID belongs to a manager with subemployees", response.getBody().getMessage());
    }

    /**
     * 
     */
    @Test
    void testUpdateManagerToEmployee_ManagerIsNotAManager() {
        Integer empId = 1;
        Integer managerId = 2;

        Employee employee = new Employee();
        employee.setEmpId(empId);

        Employee manager = new Employee();
        manager.setEmpId(managerId);
        manager.setManager(new Employee()); // Invalid manager (already has a manager)

        when(employeeRepository.findById(empId)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(employeeRepository.findByManager(employee)).thenReturn(Collections.emptyList());

        ResponseEntity<EmployeeResponseUpdate> response = aspireService.updateManagerToEmployee(empId, managerId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The manager ID doesn't belongs to a manager", response.getBody().getMessage());
    }
}
