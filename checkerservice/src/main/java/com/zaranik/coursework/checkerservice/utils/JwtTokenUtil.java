package com.zaranik.coursework.checkerservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaranik.coursework.checkerservice.exceptions.AccessTokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

  private final ObjectMapper objectMapper;

  private String decodeToJson(final String base64) {
    return StringUtils.newStringUtf8(Base64.decodeBase64(base64));
  }

  public String getUserNameFromToken(String token) {
    return getClaim(token, "sub");
  }

  public String getRoleNameFromToken(String token) {
    return getClaim(token, "role");
  }

  private String getClaim(String token, String claimName) {
    String payloadPartOfToken = token.split("\\.")[1];
    String jsonPayload = decodeToJson(payloadPartOfToken);
    try {
      JsonNode node = objectMapper.readTree(jsonPayload);
      JsonNode claim = node.get(claimName);
      if (claim == null) {
        throw new AccessTokenInvalidException();
      }
      String roleStringValue = claim.toString();
      StringBuilder sb = new StringBuilder(roleStringValue);
      sb.deleteCharAt(roleStringValue.length() - 1);
      sb.deleteCharAt(0);
      return sb.toString();
    } catch (JsonProcessingException e) {
      throw new AccessTokenInvalidException();
    }
  }

}
