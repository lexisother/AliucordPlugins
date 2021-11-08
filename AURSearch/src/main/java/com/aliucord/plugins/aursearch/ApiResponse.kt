/*
 * Alyx's Aliucord Plugins
 * Copyright (C) 2021 Alyxia Sother
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
*/
package com.aliucord.plugins.aursearch

class ApiResponse {
    @JvmField
    var resultcount: Int? = null
    @JvmField
    var results: List<Package>? = null

    class Package {
        var ID: Int? = null
        @JvmField
        var Name: String? = null
        var PackageBaseID: Int? = null
        var PackageBase: String? = null
        var Version: String? = null
        @JvmField
        var Description: String? = null
        var URL: String? = null
        var NumVotes: Int? = null
        var Popularity: Double? = null
        var OutOfDate: Int? = null
        var Maintainer: String? = null
        var FirstSubmitted: Int? = null
        var LastModified: Int? = null
        var URLPath: String? = null
    }
}