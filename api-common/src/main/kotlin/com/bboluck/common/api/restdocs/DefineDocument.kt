package com.bboluck.common.api.restdocs

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.Schema
import com.epages.restdocs.apispec.model.Constraint
import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.restdocs.snippet.AbstractDescriptor
import org.springframework.restdocs.snippet.Attributes
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.ResultActions

data class DefineDocument(
    val identifier: String = "",
    val description: String = "",
    val summary: String = "",
    val tag: String = "",
    val requestSchema: String,
    val responseSchema: String,
)

@Suppress("LongParameterList")
fun defineDocument(
    identifier: String = "",
    description: String = "",
    summary: String = "",
    tag: String = "",
    requestSchema: String,
    responseSchema: String,
) = DefineDocument(
    identifier,
    description,
    summary,
    tag,
    requestSchema,
    responseSchema,
)

fun ResultActions.andDocument(
    defineDocument: DefineDocument,
    pathParameters: Array<ParameterDescriptor> = emptyArray(),
    queryParameters: Array<ParameterDescriptor> = emptyArray(),
    requestFields: Array<FieldDescriptor> = emptyArray(),
    responseFields: Array<FieldDescriptor> = emptyArray(),
    requestHeaders: Array<HeaderDescriptor> = emptyArray(),
): ResultActions {
    val snippets = mutableListOf<Snippet>()
    if (pathParameters.isNotEmpty()) {
        snippets.add(pathParameters(*pathParameters))
    }
    if (queryParameters.isNotEmpty()) {
        snippets.add(queryParameters(*queryParameters))
    }
    if (requestFields.isNotEmpty()) {
        snippets.add(requestFields(*requestFields))
    }
    if (responseFields.isNotEmpty()) {
        snippets.add(responseFields(*responseFields))
    }
    if (requestHeaders.isNotEmpty()) {
        snippets.add(requestHeaders(*requestHeaders))
    }

    return andOpenApiDocs(
        defineDocument = defineDocument,
        pathParameters = pathParameters,
        queryParameters = queryParameters,
        requestFields = requestFields,
        responseFields = responseFields,
        requestHeaders = requestHeaders,
    )
}

fun ResultActions.andRestdocs(
    identifier: String,
    vararg snippets: Snippet
): ResultActions {
    return andDo(
        MockMvcRestDocumentation.document(
            identifier,
            *snippets,
        )
    )
}

fun ResultActions.andOpenApiDocs(
    defineDocument: DefineDocument,
    pathParameters: Array<ParameterDescriptor> = emptyArray(),
    queryParameters: Array<ParameterDescriptor> = emptyArray(),
    requestFields: Array<FieldDescriptor> = emptyArray(),
    responseFields: Array<FieldDescriptor> = emptyArray(),
    requestHeaders: Array<HeaderDescriptor> = emptyArray(),
): ResultActions {
    val builder = ResourceSnippetParametersBuilder()
        .tag(defineDocument.tag)
        .description(defineDocument.description)
        .summary(defineDocument.summary)
        .requestSchema(Schema(defineDocument.requestSchema))
        .responseSchema(Schema(defineDocument.responseSchema))

    val snippets = mutableListOf<Snippet>()

    if (pathParameters.isNotEmpty()) {
        builder.pathParameters(*pathParameters)
        snippets.add(pathParameters(*pathParameters))
    }

    if (queryParameters.isNotEmpty()) {
        builder.queryParameters(*queryParameters)
        snippets.add(queryParameters(*queryParameters))
    }

    if (requestFields.isNotEmpty()) {
        builder.requestFields(*requestFields)
        snippets.add(requestFields(*requestFields))
    }

    if (responseFields.isNotEmpty()) {
        builder.responseFields(*responseFields)
        snippets.add(responseFields(*responseFields))
    }

    if (requestHeaders.isNotEmpty()) {
        builder.requestHeaders(*requestHeaders)
        snippets.add(requestHeaders(*requestHeaders))
    }

    return andDo(
        MockMvcRestDocumentationWrapper.document(
            identifier = defineDocument.identifier,
            requestPreprocessor = preprocessRequest(prettyPrint()),
            responsePreprocessor = preprocessResponse(prettyPrint()),
            resourceDetails = builder,
            snippets = snippets.toTypedArray()
        )
    )
}

fun field(path: String): FieldDescriptor {
    return PayloadDocumentation.fieldWithPath(path)
}

fun header(name: String): HeaderDescriptor {
    return HeaderDocumentation.headerWithName(name)
}

fun <T> HeaderDescriptor.enum(enumValues: Array<T>): HeaderDescriptor {
    return this.attributes(Attributes.key("enumValues").value(enumValues))
}

fun param(name: String): ParameterDescriptor {
    return RequestDocumentation.parameterWithName(name)
}

fun <T : AbstractDescriptor<T>> T.desc(desc: String): T {
    return this.description(desc)
}

fun FieldDescriptor.string(): FieldDescriptor {
    return this.type(JsonFieldType.STRING)
}

fun FieldDescriptor.number(): FieldDescriptor {
    return this.type(JsonFieldType.NUMBER)
}

fun FieldDescriptor.nil(): FieldDescriptor {
    return this.type(JsonFieldType.NULL)
}

fun FieldDescriptor.obj(): FieldDescriptor {
    return this.type(JsonFieldType.OBJECT)
}

fun FieldDescriptor.boolean(): FieldDescriptor {
    return this.type(JsonFieldType.BOOLEAN)
}

fun FieldDescriptor.array(): FieldDescriptor {
    return this.type(JsonFieldType.ARRAY)
}

fun <T> FieldDescriptor.arrayEnum(enumValues: Array<T>): FieldDescriptor {
    return this.type(JsonFieldType.ARRAY)
        .attributes(Attributes.Attribute("itemsType", "enum"), Attributes.Attribute("enumValues", enumValues))
}

fun FieldDescriptor.arrayString(): FieldDescriptor {
    return this.type(JsonFieldType.ARRAY).attributes(Attributes.Attribute("itemsType", "string"))
}

fun FieldDescriptor.arrayNumber(): FieldDescriptor {
    return this.type(JsonFieldType.ARRAY).attributes(Attributes.Attribute("itemsType", "number"))
}

fun FieldDescriptor.varies(): FieldDescriptor {
    return this.type(JsonFieldType.VARIES)
}

fun FieldDescriptor.required(): FieldDescriptor {
    return this.attributes(
        Attributes.key("validationConstraints").value(
            listOf(Constraint("javax.validation.constraints.NotNull", mapOf()))
        )
    )
}

// EnumFields
fun <T> FieldDescriptor.enum(enumValues: Array<T>): FieldDescriptor {
    return this.type("enum").values(enumValues)
}

fun <T> FieldDescriptor.values(enumValues: Array<T>): FieldDescriptor {
    return this.attributes(Attributes.key("enumValues").value(enumValues))
}
