package wang.ismy.alibaba.consumer;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.annotations.Beta;

import javax.sql.DataSource;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
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
    public static class Api {

        @Autowired
        private ProviderClient client;

        @Autowired
        ApplicationContext applicationContext;

        @Autowired
        JdbcTemplate jdbcTemplate;

        @GetMapping("/")
        public String home() {
            return client.name();
        }

        @GetMapping("/config")
        public String config(){
            return applicationContext.getEnvironment().getProperty("user.name");
        }

        @GlobalTransactional
        @Transactional(rollbackFor = Exception.class)
        @GetMapping("order")
        public void order(Integer id){
            jdbcTemplate.update("INSERT INTO test VALUES(?)", id);
            client.order(id);
        }
    }
}
