package wang.ismy.alibaba.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author MY
 * @date 2020/4/1 19:03
 */
@FeignClient("provider")
public interface ProviderClient {
    @GetMapping("/name")
    String name();
}
