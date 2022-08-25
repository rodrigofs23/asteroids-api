package com.asteroids.api.dataclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AsteroidsNeowsCache {

  private final Cache<String, JsonNode> asteroidCache;

  private final AsteroidsNeowsClient asteroidsClient;

  @Autowired
  public AsteroidsNeowsCache(
      @Value("${nasa-api.ttl-sec}") long asteroidCacheTTL,
      @Value("${nasa-api.cache-max-size}") int asteroidCacheMaxSize,
      AsteroidsNeowsClient asteroidsClient) {

    this.asteroidCache =
        Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(asteroidCacheTTL))
            .maximumSize(asteroidCacheMaxSize)
            .build();
    this.asteroidsClient = asteroidsClient;
  }

  public JsonNode getAsteroids(String startDate, String endDate) {
    return asteroidCache.get(
        String.format("%s:%s", startDate, endDate),
        key -> asteroidsClient.getAsteroids(startDate, endDate));
  }
}
