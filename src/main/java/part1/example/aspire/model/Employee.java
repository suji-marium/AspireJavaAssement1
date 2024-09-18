package part1.example.aspire.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
    private int empId;
    private int managerId;
    private int accountId;
    private int streamId;
    private String empName;
    private String designation;
}
