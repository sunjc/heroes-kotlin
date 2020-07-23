package io.itrunner.heroes.controller

import io.itrunner.heroes.dto.HeroDto
import io.itrunner.heroes.extension.asJson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(properties = ["spring.datasource.initialization-mode=never"])
@AutoConfigureMockMvc
class HeroControllerTests(@Autowired val mockMvc: MockMvc) {

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun crudSuccess() {
        var hero = HeroDto("Jack")

        // add hero
        mockMvc.perform(
            post("/api/heroes").content(asJson(hero)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{'id':1, 'name':'Jack'}"))

        // update hero
        hero = HeroDto("Jacky", 1)
        mockMvc.perform(
            put("/api/heroes").content(asJson(hero)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("{'name':'Jacky'}"))

        // find heroes by name
        mockMvc.perform(get("/api/heroes/?name=m").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

        // get hero by id
        mockMvc.perform(get("/api/heroes/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().json("{'name':'Jacky'}"))

        // delete hero successfully
        mockMvc.perform(delete("/api/heroes/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)

        // delete hero
        mockMvc.perform(delete("/api/heroes/9999")).andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addHeroValidationFailed() {
        mockMvc.perform(post("/api/heroes").content(asJson(HeroDto())).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().`is`(400))
    }
}