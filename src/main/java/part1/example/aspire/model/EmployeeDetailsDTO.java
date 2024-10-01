package part1.example.aspire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsDTO {
    private String empName;
    private int empId;
    private String streamName;
    private String designation;
    private String accountName;
    private String managerName;
}