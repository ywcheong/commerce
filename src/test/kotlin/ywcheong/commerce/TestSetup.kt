package ywcheong.commerce

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Service
import org.springframework.test.context.TestConstructor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@WebMvcTest(includeFilters = [ComponentScan.Filter(Service::class)])
@Import(TestSetupBeans::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
annotation class TestSetup
