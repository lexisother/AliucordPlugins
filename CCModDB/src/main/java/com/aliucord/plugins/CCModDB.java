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
import com.aliucord.plugins.ccmoddb.ApiResponse;
import com.aliucord.utils.ReflectUtils;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.api.commands.CommandChoice;
import com.discord.api.message.embed.MessageEmbed;
import com.discord.models.commands.ApplicationCommandOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

@SuppressWarnings("unused")
public class CCModDB extends Plugin {
    @NonNull
    @Override
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Alyxia", 465702500146610176L) };
        manifest.description = "CCModDB";
        manifest.version = "1.1.1";
        manifest.updateUrl = "https://raw.githubusercontent.com/lexisother/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    public String result = null;
    public ApiResponse res = null;

    @Override
    public void start(Context context) {
        new Thread(() -> {
            String baseURL = "https://raw.githubusercontent.com/CCDirectLink/CCModDB/master/mods.json";
            var choices = new ArrayList<CommandChoice>();
            try {
                result = "I found the following:";
                res = Http.simpleJsonGet(baseURL, ApiResponse.class);
                for (String choice : res.mods.keySet()) {
                    var commandChoice = new CommandChoice();
                    try {
                        ReflectUtils.setField(commandChoice, "name", choice, true);
                        ReflectUtils.setField(commandChoice, "value", choice, true);
                    } catch (IllegalAccessException | NoSuchFieldException ignored) {}
                    choices.add(commandChoice);
                }
            } catch (IOException t) {
                result = "Something went wrong: " + t.getMessage();
            }
            var arguments = new ArrayList<ApplicationCommandOption>();
            arguments.add(new ApplicationCommandOption(ApplicationCommandType.STRING, "query", "What to search for.", null, true, true, choices, null));

            commands.registerCommand(
                    "ccmoddb",
                    "Searches the CCModDB.",
                    arguments,
                    args -> {
                        String query = (String) args.get("query");
                        ApiResponse.Mod finalresult = res.mods.get(query);
                        MessageEmbed finishedEmbed = null;

                        assert finalresult != null;
                        int index = finalresult.archive_link.indexOf("/releases");
                        index = index < 0 ? finalresult.archive_link.indexOf("/archive") : index;

                        var embed = new MessageEmbedBuilder()
                                .setAuthor("CCModDB", "https://avatars.githubusercontent.com/u/24706696", "https://avatars.githubusercontent.com/u/24706696")
                                .setTitle(finalresult.name)
                                .setDescription(String.format("%s\nVersion: %s\n[Link](%s)", finalresult.description, finalresult.version, finalresult.archive_link.substring(0, index)));

                        finishedEmbed = embed.build();

                        return new CommandsAPI.CommandResult(result, Collections.singletonList(finishedEmbed), false);
                    }
            );
        }).start();
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
    }
}
