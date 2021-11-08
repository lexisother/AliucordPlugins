/*
 * Alyx's Aliucord Plugins
 * Copyright (C) 2021 Alyxia Sother
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
*/
package com.aliucord.plugins

import android.content.Context
import com.aliucord.Utils.createCommandChoice
import com.aliucord.Utils.createCommandOption
import com.discord.api.commands.CommandChoice
import com.aliucord.Http
import com.aliucord.annotations.AliucordPlugin
import com.discord.api.commands.ApplicationCommandType
import com.aliucord.entities.CommandContext
import com.discord.api.message.embed.MessageEmbed
import com.aliucord.entities.MessageEmbedBuilder
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.Plugin
import com.aliucord.plugins.ccmoddb.Mod
import com.aliucord.plugins.ccmoddb.ModDB
import com.aliucord.utils.GsonUtils
import com.discord.models.commands.ApplicationCommandOption
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.ArrayList

@AliucordPlugin
class CCModDB : Plugin() {
    var result: String? = null
    var res: String? = null
    var final: ModDB? = null
    override fun start(context: Context) {
        Thread {
            val baseURL = "https://raw.githubusercontent.com/CCDirectLink/CCModDB/master/npDatabase.json"
            val choices = ArrayList<CommandChoice>()
            try {
                result = "I found the following:"
                res = Http.simpleGet(baseURL)
                final = GsonUtils.fromJson<ModDB>(res, TypeToken.getParameterized(Map::class.java, String::class.java, Mod::class.java).getType())
                for (choice in final!!.values) {
                    val commandChoice = createCommandChoice(choice.metadata.name, choice.metadata.name)
                    choices.add(commandChoice)
                }
            } catch (t: IOException) {
                result = "Something went wrong: " + t.message
            }
            val arguments = ArrayList<ApplicationCommandOption>()
            arguments.add(createCommandOption(ApplicationCommandType.STRING, "query", "What to search for.", null, true, true, emptyList(), choices))
            commands.registerCommand(
                    "ccmoddb",
                    "Searches the CCModDB.",
                    arguments
            ) { ctx: CommandContext ->
                val query = ctx.getRequiredString("query")
                val finalresult = final!!.getValue(query)
                var finishedEmbed: MessageEmbed? = null
                assert(finalresult != null)

                // do some garbage preprocessing
                var finalresultHomepage = finalresult!!.installation[0].url.split("/").slice(0..4).joinToString("/")

                val embed = MessageEmbedBuilder()
                        .setAuthor("CCModDB", "https://avatars.githubusercontent.com/u/24706696", "https://avatars.githubusercontent.com/u/24706696")
                        .setTitle(finalresult.metadata.name)
                        .setDescription(String.format("%s\nVersion: %s\n[Link](%s)", finalresult.metadata.description, finalresult.metadata.version, finalresultHomepage))
                finishedEmbed = embed.build()
                CommandResult(result, listOf(finishedEmbed), false)
            }
        }.start()
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
}