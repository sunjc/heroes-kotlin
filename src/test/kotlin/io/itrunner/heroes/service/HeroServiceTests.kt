package io.itrunner.heroes.service

import io.itrunner.heroes.dto.HeroDto
import io.itrunner.heroes.entity.Hero
import io.itrunner.heroes.repository.HeroRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import java.util.*

@ExtendWith(MockKExtension::class)
class HeroServiceTests {
    @MockK
    private lateinit var heroRepository: HeroRepository

    @InjectMockKs
    private lateinit var heroService: HeroService

    @BeforeEach
    fun setup() {
        val heroes: MutableList<Hero> = ArrayList()
        heroes.add(Hero("Rogue", id = 1))
        heroes.add(Hero("Jason", id = 2))
        every { heroRepository.findByIdOrNull(1) } returns heroes[0]
        every { heroRepository.findAll(PageRequest.of(0, 10)) } returns Page.empty()
        every { heroRepository.findByName("o") } returns heroes
    }

    @Test
    fun getHeroById() {
        val hero: HeroDto? = heroService.getHeroById(1)
        assertThat(hero?.name).isEqualTo("Rogue")
    }

    @Test
    fun getAllHeroes() {
        val heroes: Page<HeroDto> = heroService.getAllHeroes(PageRequest.of(0, 10))
        assertThat(heroes.totalElements).isEqualTo(0)
    }

    @Test
    fun findHeroesByName() {
        val heroes: List<HeroDto> = heroService.findHeroesByName("o")
        assertThat(heroes.size).isEqualTo(2)
    }
}