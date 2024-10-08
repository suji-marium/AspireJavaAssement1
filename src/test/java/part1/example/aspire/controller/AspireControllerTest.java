package part1.example.aspire.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import part1.example.aspire.model.EmployeeRequestDTO;
import part1.example.aspire.model.EmployeeResponseGet;
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
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setEmpName("Manju Kumar");
        requestDTO.setManagerId(1);
        EmployeeResponseUpdate responseUpdate = new EmployeeResponseUpdate("Employee added successfully"); // Fill in with expected data

        when(aspireService.addEmployee(any(EmployeeRequestDTO.class)))
                .thenReturn(new ResponseEntity<>(responseUpdate, HttpStatus.CREATED));

        ResponseEntity<EmployeeResponseUpdate> response = aspireController.addEmployee(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseUpdate, response.getBody());
    }
    
    @Test
    public void testGetEmployee() {
        String startLetter = "A";
        EmployeeResponseGet responseGet = new EmployeeResponseGet();

        when(aspireService.getEmployees(startLetter))
                .thenReturn(new ResponseEntity<>(responseGet, HttpStatus.OK));

        ResponseEntity<EmployeeResponseGet> response = aspireController.getEmployee(startLetter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseGet, response.getBody());
    }

    @Test
    public void testGetStreams() {
        StreamResponseGet responseGet = new StreamResponseGet(); 

        when(aspireService.getStream())
                .thenReturn(new ResponseEntity<>(responseGet, HttpStatus.OK));

        ResponseEntity<StreamResponseGet> response = aspireController.getStreams();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseGet, response.getBody());
    }

    @Test
    public void testUpdateEmployeeToManager() {
        Integer empId = 1;
        Integer streamId = 2;
        EmployeeResponseUpdate responseUpdate = new EmployeeResponseUpdate(); // Fill in with expected data

        when(aspireService.updateEmployeeToManager(empId, streamId))
                .thenReturn(new ResponseEntity<>(responseUpdate, HttpStatus.OK));

        ResponseEntity<EmployeeResponseUpdate> response = aspireController.updateEmployeeToManager(empId, streamId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseUpdate, response.getBody());
    }

    @Test
    public void testUpdateManagerToEmployee() {
        Integer empId = 1;
        Integer managerId = 2;
        EmployeeResponseUpdate responseUpdate = new EmployeeResponseUpdate(); // Fill in with expected data

        when(aspireService.updateManagerToEmployee(empId, managerId))
                .thenReturn(new ResponseEntity<>(responseUpdate, HttpStatus.OK));

        ResponseEntity<EmployeeResponseUpdate> response = aspireController.updateManagerToEmployee(empId, managerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseUpdate, response.getBody());
    }
}
