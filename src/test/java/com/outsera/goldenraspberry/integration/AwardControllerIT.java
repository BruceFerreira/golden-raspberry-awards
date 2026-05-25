package com.outsera.goldenraspberry.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AwardControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnJsonContentType() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturnResponseWithMinAndMaxArrays() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min").isArray())
                .andExpect(jsonPath("$.max").isArray());
    }

    @Test
    void shouldReturnThreeProducersWithMinInterval() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min", hasSize(3)));
    }

    @Test
    void shouldReturnIntervalOneForAllMinProducers() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min[*].interval", everyItem(equalTo(1))));
    }

    @Test
    void shouldReturnTwoProducersWithMaxInterval() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.max", hasSize(2)));
    }

    @Test
    void shouldReturnIntervalThirteenForAllMaxProducers() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.max[*].interval", everyItem(equalTo(13))));
    }

    @Test
    void shouldContainProducerMinWithCorrectYears() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min[?(@.producer == 'Producer Min' && @.previousWin == 1990 && @.followingWin == 1991)]").exists());
    }

    @Test
    void shouldContainProducerTieMinWithCorrectYears() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min[?(@.producer == 'Producer Tie Min' && @.previousWin == 2001 && @.followingWin == 2002)]").exists());
    }

    @Test
    void shouldContainProducerMaxWithCorrectYears() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.max[?(@.producer == 'Producer Max' && @.previousWin == 2000 && @.followingWin == 2013)]").exists());
    }

    @Test
    void shouldContainProducerTieMaxWithCorrectYears() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.max[?(@.producer == 'Producer Tie Max' && @.previousWin == 2005 && @.followingWin == 2018)]").exists());
    }

    @Test
    void shouldExcludeProducerWithSingleWin() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min[*].producer", not(hasItem("Producer Single Win"))))
                .andExpect(jsonPath("$.max[*].producer", not(hasItem("Producer Single Win"))));
    }

    @Test
    void shouldExcludeNonWinningProducers() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min[*].producer", not(hasItem("No Win Producer"))))
                .andExpect(jsonPath("$.max[*].producer", not(hasItem("No Win Producer"))));
    }

    @Test
    void shouldContainAllRequiredFieldsInMinItems() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min[0].producer").exists())
                .andExpect(jsonPath("$.min[0].interval").exists())
                .andExpect(jsonPath("$.min[0].previousWin").exists())
                .andExpect(jsonPath("$.min[0].followingWin").exists());
    }

    @Test
    void shouldContainAllRequiredFieldsInMaxItems() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.max[0].producer").exists())
                .andExpect(jsonPath("$.max[0].interval").exists())
                .andExpect(jsonPath("$.max[0].previousWin").exists())
                .andExpect(jsonPath("$.max[0].followingWin").exists());
    }

    @Test
    void shouldComputeAllConsecutiveIntervalsForThreeWinsProducer() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath(
                        "$.min[?(@.producer == 'Producer Three Wins Fast' && @.previousWin == 1993 && @.followingWin == 1994)]"
                ).exists())
                .andExpect(jsonPath(
                        "$.min[?(@.producer == 'Producer Three Wins Fast' && @.previousWin == 1994 && @.followingWin == 2006)]"
                ).isEmpty())
                .andExpect(jsonPath("$.max[*].producer", not(hasItem("Producer Three Wins Fast"))))
                .andExpect(jsonPath("$.min[*].producer", not(hasItem("Producer Three Wins"))))
                .andExpect(jsonPath("$.max[*].producer", not(hasItem("Producer Three Wins"))));
    }

    @Test
    void shouldReturnMethodNotAllowedForPost() throws Exception {
        mockMvc.perform(post("/v1/producers/awards-interval"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("METHOD_NOT_ALLOWED"));
    }

    @Test
    void shouldReturnMethodNotAllowedForDelete() throws Exception {
        mockMvc.perform(delete("/v1/producers/awards-interval"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("METHOD_NOT_ALLOWED"));
    }
}
