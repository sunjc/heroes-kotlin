package io.itrunner.heroes.config

import com.fasterxml.classmate.TypeResolver
import org.springframework.core.annotation.Order
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import springfox.documentation.builders.RequestParameterBuilder
import springfox.documentation.schema.ScalarType
import springfox.documentation.service.ParameterType
import springfox.documentation.service.RequestParameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext

@Component
@Order
class PageableParameterReader(private val resolver: TypeResolver) : OperationBuilderPlugin {

    override fun apply(context: OperationContext) {
        val methodParameters = context.parameters
        val pageableType = resolver.resolve(Pageable::class.java)
        val parameters: MutableList<RequestParameter> = mutableListOf()

        for (methodParameter in methodParameters) {
            if (methodParameter.parameterType != pageableType) {
                continue
            }
            parameters.apply {
                add(
                    RequestParameterBuilder().`in`(ParameterType.QUERY)
                        .name("page").query { q -> q.model { m -> m.scalarModel(ScalarType.INTEGER) } }
                        .description("Results page you want to retrieve (0..N)").build()
                )
                add(
                    RequestParameterBuilder().`in`(ParameterType.QUERY)
                        .name("size").query { q -> q.model { m -> m.scalarModel(ScalarType.INTEGER) } }
                        .description("Number of records per page").build()
                )
                add(
                    RequestParameterBuilder().`in`(ParameterType.QUERY)
                        .name("sort")
                        .query { q ->
                            q.model { m -> m.collectionModel { c -> c.model { cm -> cm.scalarModel(ScalarType.STRING) } } }
                        }
                        .description(
                            "Sorting criteria in the format: property(,asc|desc). "
                                    + "Default sort order is ascending. Multiple sort criteria are supported."
                        ).build()
                )
            }
            context.operationBuilder().requestParameters(parameters)
        }
    }

    override fun supports(delimiter: DocumentationType) = true
}