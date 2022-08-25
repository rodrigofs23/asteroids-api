package com.asteroids.api.service;

import com.asteroids.api.asteroid.Asteroid;
import com.asteroids.api.dataclient.AsteroidsNeowsCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsteroidServiceTest {

    @Mock
    private AsteroidsNeowsCache asteroidsNeowsCache;
    @InjectMocks
    private AsteroidService asteroidServiceUnderTest;

    ObjectMapper mapper = new ObjectMapper();
    final String EXPECTED_JSON_STRING = "{\"near_earth_objects\":{\"2015-09-08\":[{\"id\":\"2465633\",\"name\":\"465633 (2009 JR5)\",\"estimated_diameter\":{\"meters\":{\"estimated_diameter_min\":225.1930466786,\"estimated_diameter_max\":503.5469604336}},\"close_approach_data\":[{\"close_approach_date\":\"2015-09-08\",\"miss_distance\":{\"kilometers\":\"45290438.204452618\"}}]}],\"2015-09-07\":[{\"id\":\"2440012\",\"name\":\"440012 (2002 LE27)\",\"estimated_diameter\":{\"meters\":{\"estimated_diameter_min\":366.9061375314,\"estimated_diameter_max\":820.4270648822}},\"close_approach_data\":[{\"close_approach_date\":\"2015-09-07\",\"miss_distance\":{\"kilometers\":\"74525035.840942964\"}}]}]}}";

    @Test
    void getCloseAsteroids_shouldReturnAsteroidList_ifAsteroidsNeowsReturnsValidResult() throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(EXPECTED_JSON_STRING);
        List<Asteroid> expectedAsteroidList = List.of(mapper.treeToValue(jsonNode.get("near_earth_objects").get("2015-09-08").get(0), Asteroid.class),
                mapper.treeToValue(jsonNode.get("near_earth_objects").get("2015-09-07").get(0), Asteroid.class));

        when(asteroidsNeowsCache.getAsteroids(anyString(), anyString()))
                .thenReturn(jsonNode);

        final List<Asteroid> actualAsteroidList = asteroidServiceUnderTest.getClosestAsteroids("2015-09-07", "2015-09-08");

        assertEquals(expectedAsteroidList, actualAsteroidList);
    }

    @Test
    void getLargestAsteroid_shouldReturnAsteroid_ifAsteroidsNeowsReturnsValidResult() throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(EXPECTED_JSON_STRING);
        Asteroid expectedAsteroid = mapper.treeToValue(jsonNode.get("near_earth_objects").get("2015-09-07").get(0), Asteroid.class);
        when(asteroidsNeowsCache.getAsteroids(anyString(), anyString()))
                .thenReturn(jsonNode);

        final Asteroid actualAsteroid = asteroidServiceUnderTest.getLargestAsteroid("2015");

        assertEquals(expectedAsteroid, actualAsteroid);
    }
}