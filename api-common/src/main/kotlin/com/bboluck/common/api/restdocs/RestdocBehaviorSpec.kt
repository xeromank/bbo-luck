package com.bboluck.common.api.restdocs

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.extensions.spring.testContextManager
import org.springframework.context.ApplicationContext
import org.springframework.restdocs.ManualRestDocumentation

open class RestdocBehaviorSpec(
    body: BehaviorSpec.() -> Unit = {}
) : BehaviorSpec(body = body) {
    override fun extensions() = listOf(SpringRestdocTestExtension())

    override suspend fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        if (testCase.name.prefix?.startsWith("When:") == true) {
            val applicationContext: ApplicationContext = testContextManager().testContext.applicationContext
            this.getDelegate(context = applicationContext).beforeTest(this::class.java, testCase.name.testName)
        }
    }

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        if (testCase.name.prefix?.startsWith("Then:") == true) {
            val applicationContext: ApplicationContext = testContextManager().testContext.applicationContext
            this.getDelegate(context = applicationContext).afterTest()
        }
    }

    private fun getDelegate(context: ApplicationContext): ManualRestDocumentation {
        return context.getBean(ManualRestDocumentation::class.java)
    }
}
