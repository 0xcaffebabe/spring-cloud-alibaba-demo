package wang.ismy.alibaba.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MY
 * @date 2020/4/8 19:20
 */
@RestController
public class GatewayController {

    @GetMapping("/test")
    public String test(){
        return "done";
    }
}
