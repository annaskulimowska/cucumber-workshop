package com.integration.bankingapp.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class WebServiceLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        log.info("[WS-REQUEST] {} {} {}", request.getMethod(), request.getURI().getPath(), new String(body));
        ClientHttpResponse response = execution.execute(request, body);
        log.info("[WS-RESPONSE] {} {}", response.getStatusCode().value(), new String(response.getBody().readAllBytes()));
        return response;
    }
}