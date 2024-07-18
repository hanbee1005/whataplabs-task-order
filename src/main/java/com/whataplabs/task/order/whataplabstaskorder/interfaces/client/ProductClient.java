package com.whataplabs.task.order.whataplabstaskorder.interfaces.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.client.request.ProductOrderRequest;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.client.response.ProductOrderResponse;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {
    @Value("${external.client.product.host}")
    private String host;

    @Value("${external.client.product.port}")
    private String port;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ProductOrderResponse checkStockAndDeduct(ProductOrderRequest request) throws JsonProcessingException {
        String url = host + ":" + port + "/products/order";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductOrderRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<CommonResponse> response = restTemplate.postForEntity(url, entity, CommonResponse.class);
        log.info("{}", response.getBody());

        if (response.getStatusCode().isError()) {
            throw new RuntimeException("HTTP FAIL");
        }

        if (ObjectUtils.isEmpty(response.getBody().data())) {
            return ProductOrderResponse.empty();
        }

        return objectMapper.readValue(objectMapper.writeValueAsString(response.getBody().data()), ProductOrderResponse.class);
    }
}
