package io.itrunner.heroes.controller

import io.itrunner.heroes.dto.HeroDto
import io.itrunner.heroes.exception.HeroNotFoundException
import io.itrunner.heroes.extension.Messages
import io.itrunner.heroes.service.HeroService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.SortDefault
import org.springframework.data.web.SortDefault.SortDefaults
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/heroes"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Api(tags = ["Hero Controller"])
class HeroController(private val service: HeroService, private val messages: Messages) {

    @ApiOperation("Get hero by id")
    @GetMapping("/{id}")
    fun getHero(@ApiParam(required = true, example = "1") @PathVariable("id") id: Long) =
        service.getHeroById(id) ?: throw HeroNotFoundException(messages.getMessage("hero.notFound", arrayOf(id)))

    @ApiOperation("Get all heroes")
    @GetMapping
    fun getHeroes(
        @ApiIgnore @SortDefaults(SortDefault(sort = ["name"], direction = Sort.Direction.ASC)) pageable: Pageable
    ) = service.getAllHeroes(pageable)

    @ApiOperation("Search heroes by name")
    @GetMapping("/")
    fun searchHeroes(@ApiParam(required = true) @RequestParam("name") name: String) = service.findHeroesByName(name)

    @ApiOperation("Add new hero")
    @PostMapping
    fun addHero(@ApiParam(required = true) @Valid @RequestBody hero: HeroDto) = service.saveHero(hero)

    @ApiOperation("Update hero info")
    @PutMapping
    fun updateHero(@ApiParam(required = true) @Valid @RequestBody hero: HeroDto) = service.saveHero(hero)

    @ApiOperation("Delete hero by id")
    @DeleteMapping("/{id}")
    fun deleteHero(@ApiParam(required = true, example = "1") @PathVariable("id") id: Long) = service.deleteHero(id)
}