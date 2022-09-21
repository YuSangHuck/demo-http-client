package com.demo.demohttpclient.api;

import com.demo.demohttpclient.dto.ReqDTO;
import com.demo.demohttpclient.dto.ResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/test")
public class TestController {
    @RequestMapping(value = "/200")
    public ResponseEntity<ResDTO> res200() {
        log.info("request 200");
        return new ResponseEntity<>(new ResDTO(HttpStatus.OK, "request success"), HttpStatus.OK);
    }

    @RequestMapping(value = "/400")
    public ResponseEntity<ResDTO> res400() {
        log.info("request 400");
        return new ResponseEntity<>(new ResDTO(HttpStatus.BAD_REQUEST, "invalid request data"), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/500")
    public ResponseEntity<ResDTO> res500() {
        log.info("request 500");
        return new ResponseEntity<>(new ResDTO(HttpStatus.INTERNAL_SERVER_ERROR, "system error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/timeout")
    public ResponseEntity<ResDTO> timeout(ReqDTO reqDTO) throws InterruptedException {
        log.info("request timeout");
        Thread.sleep(10_000);
        log.info("wakeup");
        return new ResponseEntity<>(new ResDTO(HttpStatus.OK, "request success"), HttpStatus.OK);
    }
}
