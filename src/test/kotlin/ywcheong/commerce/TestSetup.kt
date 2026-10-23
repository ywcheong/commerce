package ywcheong.commerce

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@WebMvcTest
@Import(TestSetupBeans::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
annotation class TestSetup
