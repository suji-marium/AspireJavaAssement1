package part1.example.aspire;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AspireApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
    public void testMainMethod() {
        String[] args = {};
        
        AspireApplication.main(args);
        
    }
}
