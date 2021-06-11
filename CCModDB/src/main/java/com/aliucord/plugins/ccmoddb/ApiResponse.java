/*
 * Alyx's Aliucord Plugins
 * Copyright (C) 2021 Alyxia Sother
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
*/
package com.aliucord.plugins.ccmoddb;

import java.util.List;
import java.util.Map;

public class ApiResponse {
    public Map<String, Mod> mods;

    public static class Mod {
        public String name;
        public String description;
        public Page[] page;
        public String archive_link;
        public Hash hash;
        public String version;
    }
    public static class Page {
        public String name;
        public String url;
    }
    public static class Hash {
        public String sha256;
    }
}
