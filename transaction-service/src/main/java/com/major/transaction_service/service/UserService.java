package com.major.transaction_service.service;

import com.major.transaction_service.model.User;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

public class UserService implements UserDetailsService {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //make sure the authority is svc
        String transactionUser = "txn-service";
        String password = "1234";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(transactionUser, password);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = "http://localhost:10001/user/username" + username;
        ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET,request, JSONObject.class);

        JSONObject responseBody = null;

        if(response.getStatusCode().is2xxSuccessful()) {
            responseBody = response.getBody();
        } else {
            throw new UsernameNotFoundException("Unable to authenticate user");
        }

        assert responseBody != null;
        return User.builder()
                .username((String) responseBody.get("username"))
                .password((String) responseBody.get("password"))
                .authorities("usr")
                .build();
    }
}
