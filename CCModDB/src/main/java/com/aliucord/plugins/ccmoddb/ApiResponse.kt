/*
 * Alyx's Aliucord Plugins
 * Copyright (C) 2021 Alyxia Sother
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
*/
package com.aliucord.plugins.ccmoddb

typealias ModDB = Map<String, Mod>

data class Mod(
        val metadata: ModMeta,
        val installation: Array<ModInstallation>
)

data class ModMeta(
        val name: String,
        val version: String,
        val description: String? = null,
        val homepage: String? = null,
        val license: String? = null,
        val ccmodDependencies: Map<String, String>? = null,
)

data class ModInstallation(
        val type: String,
        val url: String,
)