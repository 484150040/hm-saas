package com.hm.digital.twin.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hm.digital.inface.biz.RedisService;
import com.hm.digital.twin.util.JwtUtils;

import static com.hm.digital.twin.util.JwtUtils.EXPIRATION;

@Component
public class JwtInterceptor implements HandlerInterceptor {
  @Autowired
  private RedisService redisService;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Map<String, Object> map = new HashMap<>();
    //跳过请求为OPTION的请求
    String method = request.getMethod();
    //判断是否是OPTIONS请求
    if(HttpMethod.OPTIONS.toString().equals(method)){
      return true;
    }
    String username = redisService.get("username")!=null?redisService.get("username"):null;
    String redisToken = redisService.get(username+"token")!=null?redisService.get(username+"token"):null;
    //获取请求头中令牌
    String token = request.getHeader("token");
    try {
      if (redisToken==null||!redisToken.equals(token)){
        throw new TokenExpiredException("token过期!");
      }
      Long time = System.currentTimeMillis() + EXPIRATION * 1000;
      redisService.set(username+"token",redisToken,time);
      //验证令牌
        JwtUtils.verifyToken(token);
      //验证成功，放行请求
      return true;
    } catch (SignatureVerificationException e) {
      e.printStackTrace();
      map.put("msg", "无效签名!");
    } catch (TokenExpiredException e) {
      e.printStackTrace();
      map.put("msg", "token过期!");
    } catch (AlgorithmMismatchException e) {
      e.printStackTrace();
      map.put("msg", "token算法不一致!");
    } catch (Exception e) {
      e.printStackTrace();
      map.put("msg", "token无效!!");
    }
    //设置状态
    map.put("state", false);
    //将map转为json
    String json = new ObjectMapper().writeValueAsString(map);
    // 相应json数据
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().println(json);
    return false;
  }
}
