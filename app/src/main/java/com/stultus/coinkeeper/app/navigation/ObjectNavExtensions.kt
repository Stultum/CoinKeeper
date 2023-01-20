package com.stultus.coinkeeper.app.navigation

import com.google.gson.Gson

fun <A> A.toJson(): String? =
	Gson().toJson(this)

fun <A> String.fromJson(type: Class<A>): A =
	Gson().fromJson(this, type)