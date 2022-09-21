package com.demo.demohttpclient.api;

import com.demo.demohttpclient.dto.ResDTO;
import com.demo.demohttpclient.exception.BadWebClientRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerTest {
    @Autowired
    TestController testController;
    @LocalServerPort
    private int port;
    @Autowired
    private WebClient webClient;

    private String baseUrl;

    //    TODO webClientUrl builder 자체에 뭔가 넣어줘도 되긴할듯? 뭐가나으려나
    @BeforeEach
    void setUp() {
        baseUrl = "http://127.0.0.1:" + port;
    }

    @Test
    public void contestLoads() {
        assertThat(testController).isNotNull();
    }

    private Mono<ResponseEntity<ResDTO>> retrievePostForMono(String uri, MultiValueMap<String, String> body) throws WebClientResponseException {
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

    @Test
    public void _200() {
        // given
        String URI = baseUrl + "/test/200";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        // when
        ResponseEntity resDTO = this.retrievePostForMono(URI, requestBody).block();

        // then
        assertThat(resDTO.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void _400() {
        // given
        String URI = baseUrl + "/test/400";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        // when
        BadWebClientRequestException e = assertThrows(BadWebClientRequestException.class, () -> this.retrievePostForMono(URI, requestBody).block());

        // then
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void _500() {
        // given
        String URI = baseUrl + "/test/500";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        // when
        WebClientResponseException e = assertThrows(WebClientResponseException.class, () -> this.retrievePostForMono(URI, requestBody).block());

        // then
        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    private void 생성() {
//        WebClient client1 = WebClient.create();
//        WebClient client2 = WebClient.create("http://localhost:8080");
//        WebClient client3 = WebClient.builder()
//                .baseUrl("http://localhost:8080")
//                .defaultCookie("cookieKey", "cookieValue")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
//                .build();
//
//        HttpClient httpClient = HttpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//                .responseTimeout(Duration.ofMillis(5000))
//                .doOnConnected(conn ->
//                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
//                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
//        WebClient client4 = WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .build();
//        WebClient client5 = client4.mutate()
////                ...
//                .build();
//    }
//
//    private void 요청() {
////        TODO reactor 공부. just. subscribe, block, blockOptional, ..
//        Flux<Employee> employeeFlux = 요청_GET_Flux();
//        employeeFlux.subscribe(employee -> {
//            System.out.println("employee = " + employee);
//        });
//
//        Mono<Employee> employeeMono = 요청_GET_Mono();
//        Employee block = employeeMono.block();
//        Optional<Employee> employee = employeeMono.blockOptional();
//
//        요청_POST_Mono(new Employee("gg"));
//        요청_POST_MonoEmptyReponse(new Employee("gg"));
//    }
//
//    //    GET /employees
//    private Flux<Employee> 요청_GET_Flux() {
//        return webClient.get()
//                .uri("/employees")
//                .retrieve()
//                .bodyToFlux(Employee.class);
//    }
//
//    //    GET /employees/{id}
//    private Mono<Employee> 요청_GET_Mono() {
//        return webClient.get()
//                .uri("/employees")
//                .retrieve()
//                .bodyToMono(Employee.class);
//    }
//
//    //    POST /employees
////    create from request body
////    response created employee
//    private Mono<Employee> 요청_POST_Mono(Employee employee) {
//        return webClient.post()
//                .uri("employees")
//                .body(Mono.just(employee), Employee.class)
//                .retrieve()
//                .bodyToMono(Employee.class);
//    }
//
//    //    동일하지만 response body가 empty인 경우
//    private Mono<Void> 요청_POST_MonoEmptyReponse(Employee employee) {
//        return webClient.post()
//                .uri("employees")
//                .body(Mono.just(employee), Employee.class)
//                .retrieve()
//                .bodyToMono(Void.class);
//    }
//
//    private void 응답() {
////        toEntity(), toMono(), toFlux()
//    }
}