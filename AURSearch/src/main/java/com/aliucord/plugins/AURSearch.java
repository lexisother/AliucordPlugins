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
import com.aliucord.entities.MessageEmbedBuilder;
import com.aliucord.entities.Plugin;
import com.aliucord.plugins.aursearch.ApiResponse;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.api.message.embed.MessageEmbed;
import com.discord.models.commands.ApplicationCommandOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class AURSearch extends Plugin {
    @NonNull
    @Override
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Alyxia", 465702500146610176L) };
        manifest.description = "Search the AUR.";
        manifest.version = "1.0.2";
        manifest.updateUrl = "https://raw.githubusercontent.com/lexisother/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) {
        var arguments = new ArrayList<ApplicationCommandOption>();
        arguments.add(new ApplicationCommandOption(ApplicationCommandType.STRING, "query", "What to search for.", null, true, true, null, null));

        commands.registerCommand(
                "aur",
                "Searches the AUR.",
                arguments,
                ctx -> {
                    String query = ctx.getRequiredString("query");

                    if (query == null) return new CommandsAPI.CommandResult("You did not specify a query.", null, false);

                    ApiResponse res = null;
                    MessageEmbedBuilder embed = null;
                    MessageEmbed finishedEmbed = null;
                    Integer i = 0;
                    String result;
                    try {
                        String baseURL = "https://aur.archlinux.org/rpc/?v=5&type=search&arg=";
                        String aurURL = "https://aur.archlinux.org/packages/";
                        res = Http.simpleJsonGet(baseURL + query, ApiResponse.class);
                        result = "I found the following:";

                        embed = new MessageEmbedBuilder()
                                .setColor(2266867)
                                .setTitle("AUR Search")
                                .setDescription("For all results, click [here](" + aurURL + "?K=" + query + ")")
                                .setFooter(res.resultcount + " results found, showing first 5.", "https://www.clipartmax.com/png/full/321-3216004_by-default-most-linux-distributions-including-arch-arch-linux-logo-png.png");

                        for (ApiResponse.Package pkg : res.results) {
                            if (i < 5) {
                                embed.addField(pkg.Name, pkg.Description + "\n" + String.format("[link](%s)", aurURL + pkg.Name), false);
                                i++;
                            } else {
                                break;
                            }
                        }

                        finishedEmbed = embed.build();
                    } catch (IOException t) {
                        result = "Something went wrong: " + t.getMessage();
                    }
                    return new CommandsAPI.CommandResult(result, Collections.singletonList(finishedEmbed), false);
                }
        );
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
    }
}
