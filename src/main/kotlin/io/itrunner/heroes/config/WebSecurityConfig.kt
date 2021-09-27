package io.itrunner.heroes.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val unauthorizedHandler: JwtAuthenticationEntryPoint,
    private val securityProperties: SecurityProperties,
    @Qualifier("userDetailsServiceImpl") private val userDetailsService: UserDetailsService
) : WebSecurityConfigurerAdapter() {

    private val roleAdmin = "ADMIN"

    @Value("\${api.base-path}/**")
    private val apiPath: String? = null

    @Value("\${management.endpoints.web.exposure.include}")
    private val actuatorExposures: Array<String?> = arrayOfNulls(0)

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(*securityProperties.ignorePaths.toTypedArray())
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(BCryptPasswordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // don't create session
            .authorizeRequests()
            .requestMatchers(EndpointRequest.to(*actuatorExposures)).permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(HttpMethod.POST, apiPath).hasRole(roleAdmin)
            .antMatchers(HttpMethod.PUT, apiPath).hasRole(roleAdmin)
            .antMatchers(HttpMethod.DELETE, apiPath).hasRole(roleAdmin)
            .anyRequest().authenticated().and()
            .addFilterBefore(
                authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter::class.java
            ) // Custom JWT based security filter
            .headers().cacheControl() // disable page caching
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Bean
    fun authenticationTokenFilterBean() = AuthenticationTokenFilter()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        val cors = securityProperties.cors

        with(cors) {
            configuration.allowedOrigins = allowedOrigins
            configuration.allowedMethods = allowedMethods
            configuration.allowedHeaders = allowedHeaders
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}