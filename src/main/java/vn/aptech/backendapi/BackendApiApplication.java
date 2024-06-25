package vn.aptech.backendapi;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(BackendApiApplication.class, args);
    }

}