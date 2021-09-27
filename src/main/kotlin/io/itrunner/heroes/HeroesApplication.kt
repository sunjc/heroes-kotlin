package io.itrunner.heroes

import io.itrunner.heroes.config.SecurityProperties
import io.itrunner.heroes.config.SwaggerInfoProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories("io.itrunner.heroes.repository")
@EnableConfigurationProperties(SwaggerInfoProperties::class, SecurityProperties::class)
@EntityScan(basePackages = ["io.itrunner.heroes.entity"])
class HeroesApplication

fun main(args: Array<String>) {
    runApplication<HeroesApplication>(*args)
}