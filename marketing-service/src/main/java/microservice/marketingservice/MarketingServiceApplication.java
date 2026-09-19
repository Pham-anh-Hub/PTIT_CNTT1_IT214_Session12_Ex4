package microservice.marketingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MarketingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketingServiceApplication.class, args);
    }

}
