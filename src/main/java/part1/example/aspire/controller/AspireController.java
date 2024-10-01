package part1.example.aspire.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import part1.example.aspire.model.EmployeeRequestDTO;
import part1.example.aspire.model.EmployeeResponseGet;
import part1.example.aspire.model.EmployeeResponseUpdate;
import part1.example.aspire.model.StreamResponseGet;
import part1.example.aspire.service.AspireService;

@RestController
@RequestMapping("/api/employees")
public class AspireController {
    
    private Logger logger = Logger.getLogger(AspireController.class);

    @Autowired
    private AspireService aspireService;

    @PostMapping("/addEmployee")
    public ResponseEntity<EmployeeResponseUpdate> addEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ResponseEntity<EmployeeResponseUpdate> employeeResponseUpdate= aspireService.addEmployee(employeeRequestDTO);        
        stopWatch.stop();
        logger.info("Add-Employee Query executed in " + stopWatch.getTotalTimeMillis() + "ms");

        return employeeResponseUpdate;
    }

    @GetMapping("/employee-name-start")
    public ResponseEntity<EmployeeResponseGet> getEmployee(@RequestParam (value = "startletter") String startletter){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ResponseEntity<EmployeeResponseGet> employeeResponseGet= aspireService.getEmployees(startletter);
        stopWatch.stop();
        logger.info("Get-Employee-StartWithLetter Query executed in " + stopWatch.getTotalTimeMillis() + "ms");

        return employeeResponseGet;
    }

    @GetMapping("/allstreams")
    public ResponseEntity<StreamResponseGet> getStreams(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ResponseEntity<StreamResponseGet> streamResponseGet=aspireService.getStream();
        stopWatch.stop();
        logger.info("Get-AllStream Query executed in " + stopWatch.getTotalTimeMillis() + "ms");
        return streamResponseGet;
    }

    @PutMapping("/update-to-manager")
    public ResponseEntity<EmployeeResponseUpdate> updateEmployeeToManager(@RequestParam(value = "employeeId") Integer empId,
    @RequestParam(value="streamId") Integer streamId) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ResponseEntity<EmployeeResponseUpdate> employeeResponseUpdate=aspireService.updateEmployeeToManager(empId,streamId);
        stopWatch.stop();
        logger.info("Update-To-Manager Query executed in " + stopWatch.getTotalTimeMillis() + "ms");
        return employeeResponseUpdate;
    }

    @PutMapping("/update-to-employee")
    public ResponseEntity<EmployeeResponseUpdate> updateManagerToEmployee(@RequestParam(value = "employeeId") Integer empId,
    @RequestParam(value="managerId") Integer managerId) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ResponseEntity<EmployeeResponseUpdate> employeeResponseUpdate=aspireService.updateManagerToEmployee(empId,managerId);
        stopWatch.stop();
        logger.info("Update-To-Employee Query executed in " + stopWatch.getTotalTimeMillis() + "ms");
        return employeeResponseUpdate;
    }

}
