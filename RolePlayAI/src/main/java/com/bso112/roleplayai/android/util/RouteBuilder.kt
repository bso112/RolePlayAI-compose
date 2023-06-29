package com.bso112.roleplayai.android.util

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.gson.Gson

class RouteBuilder(
    private val path: String,
    private val arguments: List<NamedNavArgument> = emptyList(),
    private val deepLinks: List<NavDeepLink> = emptyList(),
) {

    private val gson = Gson()
    fun buildRoute(params: List<Any?>) = buildString {
        append(path)
        append("?")

        validateParams(params)
        arguments.zip(params).forEach { pair ->
            val key = pair.first.name
            val value = Uri.decode(gson.toJson(pair.second).trimQuotes())
            logD("${key}=${value}")
            append("${key}=${value}")
            append("&")
        }
        if (last() == '&') {
            deleteCharAt(lastIndex)
        }
    }

    private fun buildScreenRoute() = buildString {
        append(path)
        append("?")

        arguments.forEach { arg ->
            append("${arg.name}={${arg.name}}")
            append("&")
        }
        if (last() == '&') {
            deleteCharAt(lastIndex)
        }
    }

    private fun validateParams(params: List<Any?>) {
        logD(params.filterNotNull().map { it::class.simpleName }.toString())
        logD(arguments.map { it.argument.type.name }.toString())
        require(params.size == arguments.size)
    }

    fun NavGraphBuilder.buildComposable(
        content: @Composable (NavBackStackEntry) -> Unit
    ) {
        composable(
            route = buildScreenRoute(),
            deepLinks = deepLinks,
            arguments = arguments,
            content = content
        )
    }

    private fun String.trimQuotes(): String {
        if (isEmpty()) return this
        return if (first() == '\"' && last() == '\"') {
            drop(1).dropLast(1)
        } else {
            this
        }
    }
}

