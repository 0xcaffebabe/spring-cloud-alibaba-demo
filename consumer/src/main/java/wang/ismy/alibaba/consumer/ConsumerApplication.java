package wang.ismy.alibaba.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @RestController
    public static class Api {

        @Autowired
        private ProviderClient client;

        @Autowired
        ApplicationContext applicationContext;

        @GetMapping("/")
        public String home() {
            return client.name();
        }

        @GetMapping("/config")
        public String config(){
            return applicationContext.getEnvironment().getProperty("user.name");
        }
    }
}
