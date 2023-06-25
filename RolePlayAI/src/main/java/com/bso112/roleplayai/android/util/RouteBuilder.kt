package com.bso112.roleplayai.android.util

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

class RouteBuilder(
    private val path: String,
    private val arguments: List<NamedNavArgument> = emptyList(),
    private val deepLinks: List<NavDeepLink> = emptyList(),
) {
    fun buildRoute(params: List<Any?>) = buildString {
        append(path)
        append("?")

        validateParams(params)

        arguments.zip(params).forEach { pair ->
            val param = pair.second
            append("${pair.first.name}=$param")
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
}

