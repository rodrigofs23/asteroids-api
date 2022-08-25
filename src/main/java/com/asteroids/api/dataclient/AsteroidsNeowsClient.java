package com.asteroids.api.dataclient;

import com.asteroids.api.exceptions.NasaApiException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsteroidsNeowsClient extends RestClient {

    private final String apiKey;

    public AsteroidsNeowsClient(@Value("${nasa-api.url}") final String url,
                                @Value("${nasa-api.apikey}") final String apiKey,
                                @Value("${nasa-api.max-idle-seconds}") int nasaApiMaxIdleSeconds) {
        super("nasa-api-client-connection-provider", nasaApiMaxIdleSeconds);
        this.webClient = webClient.mutate()
                .baseUrl(url)
                .build();
        this.apiKey = apiKey;
    }

    public JsonNode getAsteroids(String startDate, String endDate) {
        try {
            return webClient.get()
                    .uri("/neo/rest/v1/feed?start_date={startDate}&end_date={endDate}&api_key={apiKey}", startDate, endDate, apiKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(HttpStatus::isError, response -> {
                        log.error("Unable to get asteroids from NASA API. Response: {}", response.toString());
                        throw new NasaApiException("Unable to get asteroids from NASA API");
                    })
                    .bodyToMono(JsonNode.class)
                    .block();

        } catch (Exception e) {
            throw new NasaApiException("Unable to reach NASA API client", e);
        }
    }
}