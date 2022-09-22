package com.demo.demohttpclient.service;

import com.demo.demohttpclient.dto.ResDTO;
import com.demo.demohttpclient.exception.BadWebClientRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LocalWebClientService {
    private final WebClient webClient;

    public Mono<ResponseEntity<ResDTO>> retrievePostForMono(String uri, MultiValueMap<String, String> body) throws WebClientResponseException {
        return webClient.post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(
                                new BadWebClientRequestException(
                                        response.rawStatusCode(),
                                        response.statusCode(),
                                        String.format("4xx 오류. statusCode: %s, response: %s, header: %s",
                                                response.statusCode(),
                                                response.bodyToMono(String.class),
                                                response.headers()
                                        )
                                )
                        )
                )
                .onStatus(HttpStatus::is5xxServerError, response ->
                        Mono.error(
                                new WebClientResponseException(
                                        response.rawStatusCode(),
                                        String.format("5xx 오류. %s", response.bodyToMono(String.class)),
                                        response.headers().asHttpHeaders(),
                                        null, null
                                )
                        )
                )
                .toEntity(ResDTO.class);
    }

    public Mono<ResDTO> exchangePostForMono(String uri, MultiValueMap<String, String> body) {
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromValue(body))
                .bodyValue(body)
                .exchangeToMono(response ->
                                response.bodyToMono(ResDTO.class)
                                        .map(validReqDTO -> {
                                            if (response.statusCode().is2xxSuccessful()) {
                                                return validReqDTO;
                                            } else if (response.statusCode().is4xxClientError()) {
                                                throw new BadWebClientRequestException(
                                                        response.rawStatusCode(),
                                                        response.statusCode(),
                                                        String.format(
                                                                "4xx 외부 요청 오류. statusCode: %s, response: %s, header: %s",
                                                                response.rawStatusCode(), response.bodyToMono(String.class), response.headers().asHttpHeaders()
                                                        )
                                                );
                                            } else {
//                                response.statusCode().is5xxServerError()
                                                throw new WebClientResponseException(
                                                        response.rawStatusCode(),
                                                        String.format("5xx 외부 시스템 오류. %s", response.bodyToMono(String.class)),
                                                        response.headers().asHttpHeaders(), null, null);
                                            }
                                        })
                );
    }
}
