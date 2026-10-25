package ywcheong.commerce

import com.jayway.jsonpath.JsonPath

fun errorCode(body: String?): String? = if (body.isNullOrBlank()) null else JsonPath.read<Map<String, Any?>>(body, "\$")["code"] as String?
