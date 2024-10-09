package part1.example.aspire.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import part1.example.aspire.model.EmployeeRequestDTO;
import part1.example.aspire.model.EmployeeResponseUpdate;
import part1.example.aspire.model.StreamResponseGet;
import part1.example.aspire.service.AspireService;


public class AspireControllerTest {

    @InjectMocks
    private AspireController aspireController; 

    @Mock
    private AspireService aspireService; 

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEmployee() {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO(); 
        EmployeeResponseUpdate employeeResponseUpdate = new EmployeeResponseUpdate(); 

        when(aspireService.addEmployee(employeeRequestDTO))
                .thenReturn(new ResponseEntity<>(employeeResponseUpdate, HttpStatus.OK));

        ResponseEntity<EmployeeResponseUpdate> response = aspireController.addEmployee(employeeRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeResponseUpdate, response.getBody());
    }

    @Test
    public void testGetStreams() {
        StreamResponseGet streamResponseGet = new StreamResponseGet(); // Populate with expected response

        when(aspireService.getStream())
                .thenReturn(new ResponseEntity<>(streamResponseGet, HttpStatus.OK));

        ResponseEntity<StreamResponseGet> response = aspireController.getStreams();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(streamResponseGet, response.getBody());
    }

    @Test
    public void testUpdateEmployeeToManager() {
        Integer empId = 1;
        Integer streamId = 2;
        EmployeeResponseUpdate employeeResponseUpdate = new EmployeeResponseUpdate(); // Populate with expected response

        when(aspireService.updateEmployeeToManager(empId, streamId))
                .thenReturn(new ResponseEntity<>(employeeResponseUpdate, HttpStatus.OK));

        ResponseEntity<EmployeeResponseUpdate> response = aspireController.updateEmployeeToManager(empId, streamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeResponseUpdate, response.getBody());
    }

    @Test
    public void testUpdateManagerToEmployee() {
        Integer empId = 1;
        Integer managerId = 2;
        EmployeeResponseUpdate employeeResponseUpdate = new EmployeeResponseUpdate(); // Populate with expected response

        when(aspireService.updateManagerToEmployee(empId, managerId))
                .thenReturn(new ResponseEntity<>(employeeResponseUpdate, HttpStatus.OK));

        ResponseEntity<EmployeeResponseUpdate> response = aspireController.updateManagerToEmployee(empId, managerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeResponseUpdate, response.getBody());
    }

    // @Test
    // public void testGetEmployee() {
    //     String startLetter = "A";
    //     EmployeeResponseGet expectedResponse = new EmployeeResponseGet("Successfully fetched", new ArrayList<>());

    //     when(aspireService.getEmployees(startLetter)).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

    //     ResponseEntity<?> response = aspireController.getEmployee(startLetter);

    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(expectedResponse, response.getBody());
    // }
}
