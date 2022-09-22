package com.demo.demohttpclient.api;

import com.demo.demohttpclient.dto.ResDTO;
import com.demo.demohttpclient.service.LocalWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping(value = "/retrieve")
@RequiredArgsConstructor
public class RetrieveController {
    private final LocalWebClientService localWebClientService;

    @RequestMapping(value = "/200")
    public ResponseEntity<ResDTO> _200() {
        String uri = "/test/200";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Mono<ResponseEntity<ResDTO>> responseEntityMono = localWebClientService.retrievePostForMono(uri, body);
        ResponseEntity<ResDTO> block = responseEntityMono.block();
        return block;
    }

    @RequestMapping(value = "/400")
    public ResponseEntity<ResDTO> _400() {
        String uri = "/test/400";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Mono<ResponseEntity<ResDTO>> responseEntityMono = localWebClientService.retrievePostForMono(uri, body);
        ResponseEntity<ResDTO> block = responseEntityMono.block();
        return block;
    }

    @RequestMapping(value = "/500")
    public ResponseEntity<ResDTO> _500() {
        String uri = "/test/500";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Mono<ResponseEntity<ResDTO>> responseEntityMono = localWebClientService.retrievePostForMono(uri, body);
        ResponseEntity<ResDTO> block = responseEntityMono.block();
        return block;
    }

    @RequestMapping(value = "/timeout")
    public ResponseEntity<ResDTO> timeout() {
        String uri = "/test/timeout";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        ResponseEntity<ResDTO> block = localWebClientService.retrievePostForMono(uri, body).block();
        return block;
    }
}
