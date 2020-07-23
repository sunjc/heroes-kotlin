package io.itrunner.heroes.repository

import io.itrunner.heroes.entity.Hero
import io.itrunner.heroes.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface HeroRepository : JpaRepository<Hero, Long> {
    @Query("select h from Hero h where lower(h.name) like CONCAT('%', lower(:name), '%')")
    fun findByName(@Param("name") name: String): List<Hero>
}

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?

    @Modifying
    @Query("update User u set u.username = ?1 where u.email = ?2")
    fun updateUsername(username: String, email: String): Int
}