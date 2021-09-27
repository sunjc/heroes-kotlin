package io.itrunner.heroes.controller

import io.itrunner.heroes.dto.HeroDto
import io.itrunner.heroes.exception.HeroNotFoundException
import io.itrunner.heroes.extension.Messages
import io.itrunner.heroes.service.HeroService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.SortDefault
import org.springframework.data.web.SortDefault.SortDefaults
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/heroes"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Hero Controller")
class HeroController(private val service: HeroService, private val messages: Messages) {

    @Operation(summary = "Get hero by id")
    @GetMapping("/{id}")
    fun getHero(@Parameter(required = true, example = "1") @PathVariable("id") id: Long) =
        service.getHeroById(id) ?: throw HeroNotFoundException(messages.getMessage("hero.notFound", arrayOf(id)))

    @Operation(summary = "Get all heroes")
    @GetMapping
    fun getHeroes(
        @SortDefaults(SortDefault(sort = ["name"], direction = Sort.Direction.ASC)) @ParameterObject pageable: Pageable
    ) = service.getAllHeroes(pageable)

    @Operation(summary = "Search heroes by name")
    @GetMapping("/")
    fun searchHeroes(@Parameter(required = true) @RequestParam("name") name: String) = service.findHeroesByName(name)

    @Operation(summary = "Add new hero")
    @PostMapping
    fun addHero(@Parameter(required = true) @Valid @RequestBody hero: HeroDto) = service.saveHero(hero)

    @Operation(summary = "Update hero")
    @PutMapping
    fun updateHero(@Parameter(required = true) @Valid @RequestBody hero: HeroDto) = service.saveHero(hero)

    @Operation(summary = "Delete hero by id")
    @DeleteMapping("/{id}")
    fun deleteHero(@Parameter(required = true, example = "1") @PathVariable("id") id: Long) = service.deleteHero(id)
}