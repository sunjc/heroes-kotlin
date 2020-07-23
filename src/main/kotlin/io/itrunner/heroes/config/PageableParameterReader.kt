package io.itrunner.heroes.config

import com.fasterxml.classmate.ResolvedType
import com.fasterxml.classmate.TypeResolver
import com.google.common.base.Function
import org.springframework.core.annotation.Order
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.schema.ModelReference
import springfox.documentation.schema.ResolvedTypes
import springfox.documentation.schema.TypeNameExtractor
import springfox.documentation.service.Parameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.schema.contexts.ModelContext
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.spi.service.contexts.ParameterContext

@Component
@Order
class PageableParameterReader(private val nameExtractor: TypeNameExtractor, private val resolver: TypeResolver) :
    OperationBuilderPlugin {

    private val parameterType = "query"

    override fun apply(context: OperationContext) {
        val methodParameters = context.parameters
        val pageableType = resolver.resolve(Pageable::class.java)
        val parameters: MutableList<Parameter> = mutableListOf()

        for (methodParameter in methodParameters) {
            if (methodParameter.parameterType != pageableType) {
                continue
            }
            val parameterContext = ParameterContext(
                methodParameter,
                ParameterBuilder(),
                context.documentationContext,
                context.genericsNamingStrategy,
                context
            )
            val factory: Function<ResolvedType, out ModelReference> = createModelRefFactory(parameterContext)
            val intModel = factory.apply(resolver.resolve(Integer.TYPE))
            val stringModel = factory.apply(
                resolver.resolve(
                    MutableList::class.java, String::class.java
                )
            )
            parameters.apply {
                add(
                    ParameterBuilder().parameterType(parameterType)
                        .name("page").modelRef(intModel)
                        .description("Results page you want to retrieve (0..N)").build()
                )
                add(
                    ParameterBuilder().parameterType(parameterType)
                        .name("size").modelRef(intModel)
                        .description("Number of records per page").build()
                )
                add(
                    ParameterBuilder().parameterType(parameterType)
                        .name("sort").modelRef(stringModel).allowMultiple(true)
                        .description(
                            "Sorting criteria in the format: property(,asc|desc). "
                                    + "Default sort order is ascending. Multiple sort criteria are supported."
                        ).build()
                )
            }
            context.operationBuilder().parameters(parameters)
        }
    }

    override fun supports(delimiter: DocumentationType?) = true

    private fun createModelRefFactory(context: ParameterContext): Function<ResolvedType, out ModelReference> {
        val modelContext = with(context) {
            ModelContext.inputParam(
                groupName,
                resolvedMethodParameter().parameterType,
                documentationType,
                alternateTypeProvider,
                genericNamingStrategy,
                ignorableParameterTypes
            )
        }

        return ResolvedTypes.modelRefFactory(modelContext, nameExtractor)
    }
}