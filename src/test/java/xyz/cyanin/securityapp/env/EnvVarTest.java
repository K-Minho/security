package xyz.cyanin.securityapp.env;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EnvVarTest {

    @Test
    public void property_test(){
        String name = System.getProperty("meta.name");
        System.out.println(name);
    }
    
    @Test
    public void secrect_test(){
        String env = System.getenv("HS512_SECRET");
        System.out.println(env);
    }
}
