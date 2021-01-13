package com.changgou.filter;

import entity.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 用户认证拦截器
 */
public class AuthorizationFilter implements GlobalFilter, Ordered {
    public static final String ACCESS_TOKEN = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        //判断是否为请求登录
        String uri = request.getURI().getPath();
        //如果请求地址为登录地址，则放行
        if(uri.startsWith("/api/user/login")){
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }

        //1.从请求参数,获取token

        String token = "";

        token = request.getQueryParams().getFirst(ACCESS_TOKEN);
        //2.从请求头中获取token
        if(StringUtils.isEmpty(token)){
            token = request.getHeaders().getFirst(ACCESS_TOKEN);

        }
        //3.从cookie中获取token
        if(StringUtils.isEmpty(token)){
            token = request.getCookies().getFirst(ACCESS_TOKEN).getValue();
        }
        //验证token的合法性
        if(StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            response.setComplete();
        }else{
            try {
                JwtUtil.parseJWT(token);
                //将token封装到头中去
                request.mutate().header(ACCESS_TOKEN,token);
                //验证通过，放行
                chain.filter(exchange);
            } catch (Exception e) {
                //验证失败
                e.printStackTrace();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.setComplete();
            }
        }


        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
