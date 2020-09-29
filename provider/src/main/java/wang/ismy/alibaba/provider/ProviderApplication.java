package wang.ismy.alibaba.provider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class ProviderApplication {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {

        SpringApplication.run(ProviderApplication.class, args);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSourceProxy dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean("dataSource")
    public DataSourceProxy dataSourceProxy() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://local:3306/seata_consumer");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return new DataSourceProxy(dataSource);
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


        @GlobalTransactional
        @Transactional(rollbackFor = Exception.class)
        @GetMapping("/order")
        public void order(Integer i){
            jdbcTemplate.update("INSERT INTO test VALUES(?)", i);
        }
    }
}
