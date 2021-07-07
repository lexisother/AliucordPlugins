/*
 * Alyx's Aliucord Plugins
 * Copyright (C) 2021 Alyxia Sother
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
*/

package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;
import com.aliucord.Http;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.api.message.embed.MessageEmbed;
import com.discord.models.commands.ApplicationCommandOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unused")
public class    DiscordJS extends Plugin {
    @NonNull
    @Override
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Alyxia", 465702500146610176L) };
        manifest.description = "DiscordJS Documentation searcher.";
        manifest.version = "1.0.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/lexisother/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        var arguments = new ArrayList<ApplicationCommandOption>();
        arguments.add(new ApplicationCommandOption(ApplicationCommandType.STRING, "query", "What to search for.", null, true, true, null, null));

        commands.registerCommand(
                "docs",
                "Searches the Discord.JS docs.",
                arguments,
                ctx -> {
                    String query = ctx.getRequiredString("query");

                    if (query == null) return new CommandsAPI.CommandResult("You did not specify a query.", null, false);

                    MessageEmbed res = null;
                    String result;
                    try {
                        res = Http.simpleJsonGet("https://djsdocs.sorta.moe/v2/embed?src=master&q=" + query, MessageEmbed.class);
                        result = "I found the following:";
                    } catch (IOException t) {
                        result = "Something went wrong: " + t.getMessage();
                    }
                    return new CommandsAPI.CommandResult(result, Collections.singletonList(res), false);
                }
        );
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
    }
}
