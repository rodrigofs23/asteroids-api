package com.asteroids.api.dataclient;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

public abstract class RestClient {

  protected WebClient webClient;

  protected RestClient(final String connectionProviderName, int maxIdleSeconds) {

    ConnectionProvider connectionProvider =
        ConnectionProvider.builder(connectionProviderName)
            .maxIdleTime(Duration.ofSeconds(maxIdleSeconds))
            .build();

    this.webClient =
        WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(HttpClient.create(connectionProvider)))
            .build();
  }
}
