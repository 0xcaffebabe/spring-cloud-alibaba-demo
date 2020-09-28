package wang.ismy.alibaba.provider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        @SentinelResource(value = "resource1")
        public String name() throws InterruptedException {
            return "provider"+port; }
        public  String name(BlockException exp) throws InterruptedException {
            return "service down:"+exp;
        }
    }
}
