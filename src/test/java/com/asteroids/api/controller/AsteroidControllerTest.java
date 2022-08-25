package com.asteroids.api.controller;

import com.asteroids.api.asteroid.Asteroid;
import com.asteroids.api.asteroid.CloseApproachData;
import com.asteroids.api.asteroid.EstimatedDiameter;
import com.asteroids.api.asteroid.MissDistance;
import com.asteroids.api.service.AsteroidService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AsteroidControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private AsteroidService asteroidService;

  @Test
  void shouldReturnNotFound_ifInvalidEndpoint() throws Exception {
    mockMvc.perform(get("/asteroids/{id}", "id", "1234")).andExpect(status().isNotFound());
  }

  @Test
  void getClosestAsteroids_shouldReturnBadRequest_ifInvalidRequest() throws Exception {
    mockMvc
        .perform(get("/api/v1/asteroid/closest-asteroids", "startDate", "2019-01-01"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getClosestAsteroids_shouldReturnOk_ifValidRequest() throws Exception {
    List<CloseApproachData> closeApproachDataList =
        List.of(
            new CloseApproachData(
                new SimpleDateFormat("yyyy/MM/dd").parse("2019/01/02"), new MissDistance()));
    when(asteroidService.getClosestAsteroids(anyString(), anyString()))
        .thenReturn(
            List.of(new Asteroid(1234L, "name", closeApproachDataList, new EstimatedDiameter())));
    mockMvc
        .perform(
            get("/api/v1/asteroid/closest-asteroids")
                .param("startDate", "2019-01-01")
                .param("endDate", "2019-01-02"))
        .andExpect(status().isOk());
  }

  @Test
  void getLargestAsteroid_shouldReturnBadRequest_ifInvalidRequest() throws Exception {
    mockMvc
        .perform(get("/api/v1/asteroid/largest-asteroid", "startDate", "2019-01-01"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getLargestAsteroid_shouldReturnOk_ifValidRequest() throws Exception {
    List<CloseApproachData> closeApproachDataList =
        List.of(
            new CloseApproachData(
                new SimpleDateFormat("yyyy/MM/dd").parse("2019/01/02"), new MissDistance()));
    when(asteroidService.getLargestAsteroid(anyString()))
        .thenReturn(new Asteroid(1234L, "name", closeApproachDataList, new EstimatedDiameter()));
    mockMvc
        .perform(get("/api/v1/asteroid/largest-asteroid").param("year", "2019"))
        .andExpect(status().isOk());
  }
}
