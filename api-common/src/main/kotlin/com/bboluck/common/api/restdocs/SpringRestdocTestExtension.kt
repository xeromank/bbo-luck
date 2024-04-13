package com.bboluck.common.api.restdocs

import io.kotest.core.extensions.SpecExtension
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.extensions.spring.SpringTestContextCoroutineContextElement
import kotlinx.coroutines.withContext
import org.springframework.test.context.TestContextManager

class SpringRestdocTestExtension : TestCaseExtension, SpecExtension {
    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        return execute(testCase)
    }

    override suspend fun intercept(spec: Spec, execute: suspend (Spec) -> Unit) {
        val context = TestContextManager(spec::class.java)
        withContext(SpringTestContextCoroutineContextElement(context)) {
            super.intercept(spec, execute)
        }
    }
}
