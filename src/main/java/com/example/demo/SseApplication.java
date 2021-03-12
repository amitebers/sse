package com.example.demo;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SseApplication {

	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SseApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8082"));
        app.run(args);
    }
    
    @Bean
    public SecurityWebFilterChain sseConsumerSpringSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
            .anyExchange()
            .permitAll();
        return http.build();
    }
	@RestController
	@RequestMapping("/sse-server")
	public class ServerController {

	    @GetMapping("/stream-sse")
	    public Flux<ServerSentEvent<String>> streamEvents() {
	        return Flux.interval(Duration.ofSeconds(1))
	            .map(sequence -> ServerSentEvent.<String> builder()
	                .id(String.valueOf(sequence))
	                .event("periodic-event")
	                .data("SSE - " + LocalTime.now()
	                    .toString())
	                .build());
	    }

	    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	    public Flux<String> streamFlux() {
	        return Flux.interval(Duration.ofSeconds(1))
	            .map(sequence -> "Flux - " + LocalTime.now()
	                .toString());
	    }
	}
}
