package com.demo.demohttpclient.api;

import com.demo.demohttpclient.dto.ResDTO;
import com.demo.demohttpclient.service.LocalWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping(value = "/exchange")
@RequiredArgsConstructor
public class ExchangeController {
    private final LocalWebClientService localWebClientService;

    @RequestMapping(value = "/200")
    public ResDTO _200() {
        String uri = "/test/200";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Mono<ResDTO> resDTOMono = localWebClientService.exchangePostForMono(uri, body);
        ResDTO block = resDTOMono.block();
        return block;
    }
}
