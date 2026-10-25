package ywcheong.commerce.seller.api

import ywcheong.commerce.common.passport.Passport

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Passport
annotation class CurrentSeller
