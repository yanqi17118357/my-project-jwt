package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

@SpringBootTest
class MyProjectBackendApplicationTests {

    @Test
    void contextLoads() {
        String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoibmIiLCJuYW1lIjoibGJ3IiwiaWQiOjEsImV4cCI6NjE2NjQ4NjA4MDAsImlhdCI6MTY5MDEzMTQ3OH0.KUuGKM0OynL_DEUnRIETDBlmGjoqbt_5dP2r21ZDE1s";
        String[] split = jwtToken.split("\\.");
        for (int i = 0; i < split.length - 1; i++) {
            String s = split[i];
            byte[] decode = Base64.getDecoder().decode(s);
            System.out.println(new String(decode));
        }
    }

}
