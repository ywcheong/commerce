package ywcheong.commerce.buyer.api

import ywcheong.commerce.common.passport.Passport

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Passport
annotation class CurrentBuyer
