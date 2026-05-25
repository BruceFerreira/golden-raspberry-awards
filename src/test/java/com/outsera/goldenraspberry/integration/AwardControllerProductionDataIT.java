package com.outsera.goldenraspberry.integration;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AwardControllerProductionDataIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnOkWithJsonContentType() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturnJoelSilverAsMinIntervalProducer() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath(
                        "$.min[?(@.producer == 'Joel Silver' && @.interval == 1 && @.previousWin == 1990 && @.followingWin == 1991)]"
                ).exists());
    }

    @Test
    void shouldReturnMatthewVaughnAsMaxIntervalProducer() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath(
                        "$.max[?(@.producer == 'Matthew Vaughn' && @.interval == 13 && @.previousWin == 2002 && @.followingWin == 2015)]"
                ).exists());
    }

    @Test
    void shouldReturnMinIntervalOfOne() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min[*].interval", everyItem(equalTo(1))));
    }

    @Test
    void shouldReturnMaxIntervalOfThirteen() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.max[*].interval", everyItem(equalTo(13))));
    }

    @Test
    void shouldContainAllRequiredFieldsInResponse() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min").isArray())
                .andExpect(jsonPath("$.max").isArray())
                .andExpect(jsonPath("$.min[0].producer").exists())
                .andExpect(jsonPath("$.min[0].interval").exists())
                .andExpect(jsonPath("$.min[0].previousWin").exists())
                .andExpect(jsonPath("$.min[0].followingWin").exists())
                .andExpect(jsonPath("$.max[0].producer").exists())
                .andExpect(jsonPath("$.max[0].interval").exists())
                .andExpect(jsonPath("$.max[0].previousWin").exists())
                .andExpect(jsonPath("$.max[0].followingWin").exists());
    }

    @Test
    void shouldHavePreviousWinLessThanFollowingWinForAllItems() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(status().isOk())
                .andReturn();

        DocumentContext ctx = JsonPath.parse(result.getResponse().getContentAsString());

        List<Integer> minPrev     = ctx.read("$.min[*].previousWin");
        List<Integer> minFollowing = ctx.read("$.min[*].followingWin");
        List<Integer> maxPrev     = ctx.read("$.max[*].previousWin");
        List<Integer> maxFollowing = ctx.read("$.max[*].followingWin");

        assertFalse(minPrev.isEmpty(), "min list must not be empty");
        assertFalse(maxPrev.isEmpty(), "max list must not be empty");

        for (int i = 0; i < minPrev.size(); i++) {
            assertTrue(minFollowing.get(i) > minPrev.get(i),
                    "min[" + i + "]: followingWin=" + minFollowing.get(i)
                            + " must be greater than previousWin=" + minPrev.get(i));
        }
        for (int i = 0; i < maxPrev.size(); i++) {
            assertTrue(maxFollowing.get(i) > maxPrev.get(i),
                    "max[" + i + "]: followingWin=" + maxFollowing.get(i)
                            + " must be greater than previousWin=" + maxPrev.get(i));
        }
    }

    @Test
    void shouldReturnPositiveIntervalsOnly() throws Exception {
        mockMvc.perform(get("/v1/producers/awards-interval"))
                .andExpect(jsonPath("$.min[*].interval", everyItem(greaterThan(0))))
                .andExpect(jsonPath("$.max[*].interval", everyItem(greaterThan(0))));
    }
}
