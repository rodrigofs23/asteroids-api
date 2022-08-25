package com.asteroids.api.dataclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsteroidsNeowsCacheTest {

    @Mock
    private AsteroidsNeowsClient asteroidsNeowsClient;
    private AsteroidsNeowsCache asteroidsNeowsCache;

    ObjectMapper mapper = new ObjectMapper();
    final String EXPECTED_JSON_STRING = "{\"near_earth_objects\":{\"2015-09-08\":[{\"id\":\"2465633\",\"name\":\"465633 (2009 JR5)\",\"estimated_diameter\":{\"meters\":{\"estimated_diameter_min\":225.1930466786,\"estimated_diameter_max\":503.5469604336}},\"close_approach_data\":[{\"close_approach_date\":\"2015-09-08\",\"miss_distance\":{\"kilometers\":\"45290438.204452618\"}}]}],\"2015-09-07\":[{\"id\":\"2440012\",\"name\":\"440012 (2002 LE27)\",\"estimated_diameter\":{\"meters\":{\"estimated_diameter_min\":366.9061375314,\"estimated_diameter_max\":820.4270648822}},\"close_approach_data\":[{\"close_approach_date\":\"2015-09-07\",\"miss_distance\":{\"kilometers\":\"74525035.840942964\"}}]}]}}";

    @BeforeEach
    void setUp() {
        asteroidsNeowsCache = new AsteroidsNeowsCache(1, 2, asteroidsNeowsClient);
    }

    @Test
    void getAsteroids_shouldCallClientOnlyOnce_ifMultipleRequestsMadeBeforeCacheExpires() throws JsonProcessingException {
        when(asteroidsNeowsClient.getAsteroids(anyString(), anyString())).thenReturn(mapper.readTree(EXPECTED_JSON_STRING));

        asteroidsNeowsCache.getAsteroids("2015-09-08", "2015-09-07");
        asteroidsNeowsCache.getAsteroids("2015-09-08", "2015-09-07");

        verify(asteroidsNeowsClient, times(1)).getAsteroids("2015-09-08", "2015-09-07");
    }

    @Test
    void getAsteroids_shouldCallClientTwice_ifCacheWasExpired() throws JsonProcessingException {
        when(asteroidsNeowsClient.getAsteroids(anyString(), anyString())).thenReturn(mapper.readTree(EXPECTED_JSON_STRING));

        asteroidsNeowsCache.getAsteroids("2015-09-08", "2015-09-07");
        verify(asteroidsNeowsClient, after(1000).times(1)).getAsteroids("2015-09-08", "2015-09-07");

        asteroidsNeowsCache.getAsteroids("2015-09-08", "2015-09-07");
        verify(asteroidsNeowsClient, after(2000).times(2)).getAsteroids("2015-09-08", "2015-09-07");
    }
}