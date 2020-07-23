package io.itrunner.heroes.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(
    name = "USERS",
    uniqueConstraints = [
        (UniqueConstraint(name = "UK_USERS_USERNAME", columnNames = ["USERNAME"])),
        (UniqueConstraint(name = "UK_USERS_EMAIL", columnNames = ["EMAIL"]))
    ]
)
class User(
    @Column(name = "USERNAME", length = 50, nullable = false)
    var username: String,

    @Column(name = "PASSWORD", length = 100, nullable = false)
    var password: String,

    @Column(name = "EMAIL", length = 50, nullable = false)
    var email: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USER_AUTHORITY",
        joinColumns = [(JoinColumn(
            name = "USER_ID", referencedColumnName = "ID", foreignKey = ForeignKey(name = "FK_USER_ID")
        ))],
        inverseJoinColumns = [(JoinColumn(
            name = "AUTHORITY_ID", referencedColumnName = "ID", foreignKey = ForeignKey(name = "FK_AUTHORITY_ID")
        ))]
    )
    var authorities: MutableList<Authority> = mutableListOf(),

    @Column(name = "ENABLED")
    var enabled: Boolean = true,

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 1)
    var id: Long? = null
)

@Entity
@Table(name = "AUTHORITY")
class Authority(
    @Column(name = "AUTHORITY_NAME", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    var name: AuthorityName,

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    var users: MutableList<User>? = mutableListOf(),

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHORITY_SEQ")
    @SequenceGenerator(name = "AUTHORITY_SEQ", sequenceName = "AUTHORITY_SEQ", allocationSize = 1)
    var id: Long? = null
)

enum class AuthorityName {
    ROLE_USER, ROLE_ADMIN
}

@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(
    name = "HERO",
    uniqueConstraints = [(UniqueConstraint(name = "UK_HERO_NAME", columnNames = ["HERO_NAME"]))]
)
class Hero(
    @Column(name = "HERO_NAME", length = 30, nullable = false)
    var name: String,

    @Column(name = "CREATED_BY", length = 50, updatable = false, nullable = false)
    @CreatedBy
    var createdBy: String = "test",

    @Column(name = "CREATED_DATE", updatable = false, nullable = false)
    @CreatedDate
    var createdDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "LAST_MODIFIED_BY", length = 50)
    @LastModifiedBy
    var lastModifiedBy: String? = null,

    @Column(name = "LAST_MODIFIED_DATE")
    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null,

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HERO_SEQ")
    @SequenceGenerator(name = "HERO_SEQ", sequenceName = "HERO_SEQ", allocationSize = 1)
    var id: Long? = null
)