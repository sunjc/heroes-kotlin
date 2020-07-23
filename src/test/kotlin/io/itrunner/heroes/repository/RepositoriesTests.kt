package io.itrunner.heroes.repository

import io.itrunner.heroes.entity.Hero
import io.itrunner.heroes.entity.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val heroRepository: HeroRepository,
    val userRepository: UserRepository
) {

    @Test
    fun `when findByName then return Heroes`() {
        val jason = Hero("Jason")
        entityManager.persist(jason)
        entityManager.flush()
        val list = heroRepository.findByName("jason")
        assertThat(list.size).isEqualTo(1)
    }

    @Test
    fun `when findByUsername then return User`() {
        val lily = User("lily", "123456", "lily@163.com")
        entityManager.persist(lily)
        entityManager.flush()
        val user = userRepository.findByUsername("lily")
        assertThat(user).isEqualTo(lily)
    }
}
