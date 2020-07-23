package io.itrunner.heroes

import io.itrunner.heroes.dto.AuthenticationRequest
import io.itrunner.heroes.dto.AuthenticationResponse
import io.itrunner.heroes.dto.HeroDto
import io.itrunner.heroes.exception.ErrorMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HeroesApplicationTests(@Autowired val restTemplate: TestRestTemplate) {

    @BeforeAll
    fun beforeAll() {
        val authentication = AuthenticationRequest("admin", "admin")
        val token = restTemplate.postForObject("/api/auth", authentication, AuthenticationResponse::class.java).token
        restTemplate.restTemplate.interceptors =
            listOf(ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution ->
                val headers = request.headers
                headers.add("Authorization", "Bearer $token")
                headers.add("Content-Type", "application/json")
                execution.execute(request, body)
            })

    }

    @Test
    fun `login failed`() {
        val request = AuthenticationRequest("admin", "111111")
        val response = restTemplate.postForEntity("/api/auth", request, HttpEntity::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `crud should be executed successfully`() {
        var hero = HeroDto("Jack")

        // add hero
        hero = restTemplate.postForObject("/api/heroes", hero, HeroDto::class.java)
        assertThat(hero.id).isNotNull()

        // update hero
        val requestEntity = HttpEntity(HeroDto("Jacky"))
        hero = restTemplate.exchange("/api/heroes", HttpMethod.PUT, requestEntity, HeroDto::class.java).body!!
        assertThat(hero.name).isEqualTo("Jacky")

        // find heroes by name
        val heroes = restTemplate.getForObject("/api/heroes/?name=m", List::class.java)
        assertThat(heroes.size).isEqualTo(5)

        // get hero by id
        hero = restTemplate.getForObject("/api/heroes/${hero.id}", HeroDto::class.java)
        assertThat(hero.name).isEqualTo("Jacky")

        // delete hero successfully
        var response = restTemplate.exchange("/api/heroes/${hero.id}", HttpMethod.DELETE, null, String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        // delete hero
        response = restTemplate.exchange("/api/heroes/9999", HttpMethod.DELETE, null, String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `validation failed`() {
        val responseEntity: ResponseEntity<ErrorMessage> =
            restTemplate.postForEntity("/api/heroes", HeroDto(), ErrorMessage::class.java)
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(responseEntity.body?.error).isEqualTo("MethodArgumentNotValidException")
    }

}
