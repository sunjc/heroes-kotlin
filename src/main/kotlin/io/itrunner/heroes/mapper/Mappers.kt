package io.itrunner.heroes.mapper

import io.itrunner.heroes.dto.HeroDto
import io.itrunner.heroes.entity.Hero

fun Hero.toHeroDto() = HeroDto(name, createdBy, createdDate, id)

fun HeroDto.toHero() = Hero(name = name, id = id)