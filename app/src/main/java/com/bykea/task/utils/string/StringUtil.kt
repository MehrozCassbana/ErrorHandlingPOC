package com.bykea.task.utils.string

class StringUtil {

    fun lowerCamelCaseName(vararg nameParts: String?): String {
        val nonEmptyParts = nameParts.mapNotNull { it?.takeIf(String::isNotEmpty) }
        return nonEmptyParts.drop(1).joinToString(
            separator = "",
            prefix = nonEmptyParts.firstOrNull().orEmpty(),
            transform = String::capitalize
        )
    }

    fun dashSeparatedName(nameParts: Iterable<String?>) =
        dashSeparatedName(*nameParts.toList().toTypedArray())

    private fun dashSeparatedName(vararg nameParts: String?): String {
        val nonEmptyParts = nameParts.mapNotNull { it?.takeIf(String::isNotEmpty) }
        return nonEmptyParts.joinToString(separator = "-")
    }

}