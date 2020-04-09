package wang.ismy.alibaba.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author MY
 * @date 2020/4/7 14:06
 */
@Component
public class TokenFilter implements GlobalFilter {

    @Value("${server.port}")
    private String port;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var token = exchange.getRequest().getQueryParams().get("token");
        if (!CollectionUtils.isEmpty(token)){
            return chain.filter(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        String s = "token is null:"+port;
        return response.writeWith(
                Mono.just(
                        response.bufferFactory().wrap(s.getBytes())
                )
        );
    }
}
