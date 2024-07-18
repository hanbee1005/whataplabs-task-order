package com.whataplabs.task.order.whataplabstaskorder.interfaces.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderFailException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    public ProductOrderResponse checkStockAndDeduct(ProductOrderRequest request) {
        String url = host + ":" + port + "/products/stock/deduct";
        return checkStock(url, request);
    }

    public ProductOrderResponse checkStockAndRestock(ProductOrderRequest request) {
        String url = host + ":" + port + "/products/stock/add";
        return checkStock(url, request);
    }

    public ProductOrderResponse checkStock(String url, ProductOrderRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductOrderRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<CommonResponse> response = restTemplate.postForEntity(url, entity, CommonResponse.class);
            log.info("{}", response.getBody());

            if (response.getStatusCode().isError()) {
                handleErrorResponse(request, response.getBody());
            }

            return parseResponse(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP Status Code: {}, Response Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            handleException(e, request);
            throw new RuntimeException("PRODUCT SERVER ERROR");
        }
    }

    private <T> ProductOrderResponse parseResponse(CommonResponse<T> response) {
        if (ObjectUtils.isEmpty(response)) {
            throw new RuntimeException("RESPONSE BODY EMPTY");
        }

        if (ObjectUtils.isEmpty(response.data())) {
            return ProductOrderResponse.empty();
        }

        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(response.data()), ProductOrderResponse.class);
        } catch (JsonProcessingException e) {
            log.error("[ProductClient.parsingError] {}", e.getMessage());
            return ProductOrderResponse.empty();
        }
    }

    private <T> void handleErrorResponse(ProductOrderRequest request, CommonResponse<T> response) {
        if (ObjectUtils.isEmpty(response)) {
            throw new RuntimeException("PRODUCT CLIENT RESPONSE EMPTY");
        }

        List<String> errorType = List.of("NOT_FOUND_PRODUCT", "INSUFFICIENT_STOCK");
        if (errorType.contains(response.code())) {
            throw new OrderFailException(request.orderId(), response.message());
        }

        throw new RuntimeException("PRODUCT SERVER ERROR");
    }

    private void handleException(HttpStatusCodeException e, ProductOrderRequest request) {
        String responseBody = e.getResponseBodyAsString();

        if (responseBody.contains("NOT_FOUND_PRODUCT")) {
            throw new OrderFailException(request.orderId(), "Product not found");
        } else if (responseBody.contains("INSUFFICIENT_STOCK")) {
            throw new OrderFailException(request.orderId(), "Insufficient stock");
        }
    }
}
