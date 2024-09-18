package part1.example.aspire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import part1.example.aspire.model.Employee;
import part1.example.aspire.model.EmployeeResponseGet;
import part1.example.aspire.model.EmployeeResponseUpdate;
import part1.example.aspire.service.AspireService;

@RestController
@RequestMapping("/api/employees")
public class AspireController {
    @Autowired
    private AspireService aspireService;

    @PostMapping("/addEmployee")
    public ResponseEntity<EmployeeResponseUpdate> addEmployee(@RequestBody Employee employee) {
        return aspireService.addEmployee(employee);
    }

    @GetMapping("/employee-name-start")
    public ResponseEntity<EmployeeResponseGet> getEmployee(@RequestParam(value = "startletter") String startletter) {
        System.out.println(startletter);
        return aspireService.getEmployee(startletter);
    }

}
