package logistics;

import logistics.controllers.DeliveryController;
import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by alien on 6/14/17.
 */

@EnableJSONDoc
@SpringBootApplication
@ComponentScan(basePackageClasses = DeliveryController.class)
public class LogisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class, args);
    }
}