package part1.example.aspire.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDTO {
    
    @NotBlank(message = "Employee name is mandatory")
    private String empName;

    private Integer streamId;

    private Integer managerId;

}
