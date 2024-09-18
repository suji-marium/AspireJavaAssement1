package part1.example.aspire.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Stream {
    @Id
    private int streamId;
    private String streamName;
    private String accountId;
    private String managerId;
}
