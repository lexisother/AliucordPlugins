/*
 * Alyx's Aliucord Plugins
 * Copyright (C) 2021 Alyxia Sother
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
*/
package com.aliucord.plugins.aursearch;

import java.util.List;

public class ApiResponse {
    public Integer resultcount;
    public List<Package> results;

    public static class Package {
        public Integer ID;
        public String Name;
        public Integer PackageBaseID;
        public String PackageBase;
        public String Version;
        public String Description;
        public String URL;
        public Integer NumVotes;
        public Double Popularity;
        public Integer OutOfDate;
        public String Maintainer;
        public Integer FirstSubmitted;
        public Integer LastModified;
        public String URLPath;
    }
}