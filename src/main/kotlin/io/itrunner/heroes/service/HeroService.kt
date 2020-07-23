package io.itrunner.heroes.service

import io.itrunner.heroes.dto.HeroDto
import io.itrunner.heroes.mapper.toHero
import io.itrunner.heroes.mapper.toHeroDto
import io.itrunner.heroes.repository.HeroRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class HeroService(private val repository: HeroRepository) {
    fun getHeroById(id: Long): HeroDto? {
        return repository.findByIdOrNull(id)?.toHeroDto()
    }

    fun getAllHeroes(pageable: Pageable): Page<HeroDto> {
        return repository.findAll(pageable).map { it.toHeroDto() }
    }

    fun findHeroesByName(name: String): List<HeroDto> {
        return repository.findByName(name).map { it.toHeroDto() }
    }

    @Transactional
    fun saveHero(hero: HeroDto): HeroDto {
        return repository.save(hero.toHero()).toHeroDto()
    }

    @Transactional
    fun deleteHero(id: Long) {
        repository.deleteById(id)
    }
}