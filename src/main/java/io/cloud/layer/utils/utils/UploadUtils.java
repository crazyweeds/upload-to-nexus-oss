package io.cloud.layer.utils.utils;

import io.cloud.layer.utils.beans.Pom;
import org.codehaus.plexus.util.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @author RippleChan
 * @date 2019-09-11 00:47
 */
public class UploadUtils {

    public static void upload(Pom pom, String url, String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", null);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, createHeaders(username, password));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        System.out.println(responseEntity.getBody());
    }

    public static HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }

}
