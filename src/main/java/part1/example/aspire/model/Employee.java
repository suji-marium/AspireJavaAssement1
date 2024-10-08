package part1.example.aspire.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empId;
    
    @NotBlank(message = "Employee name is mandatory")
    private String empName;

    @ManyToOne
    @JoinColumn(name = "stream_id")
    @JsonIgnore
    private Stream stream;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @Pattern(regexp = "Manager|Associate", message = "Invalid designation")
    private String designation;

}
