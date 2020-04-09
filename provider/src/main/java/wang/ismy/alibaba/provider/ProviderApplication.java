package wang.ismy.alibaba.provider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class ProviderApplication {

    @Autowired
    ApplicationContext applicationContext;

    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @RestController
    public class ServiceApi {
        @GetMapping("/name")
        @SentinelResource(value = "test-resource",blockHandlerClass = {ServiceFallback.class})
        public String name() { return "provider"+port; }
    }
    public static class ServiceFallback {
        public static String name() {
            return "service down";
        }
    }
}
