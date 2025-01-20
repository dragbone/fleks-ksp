package com.dragbone

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class EKSystem(val required: Array<KClass<*>> = [], val optional: Array<KClass<*>> = [])
