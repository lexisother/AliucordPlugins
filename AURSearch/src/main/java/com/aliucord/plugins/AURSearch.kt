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
import com.aliucord.Utils.createCommandOption
import com.discord.api.commands.ApplicationCommandType
import com.aliucord.entities.CommandContext
import com.aliucord.plugins.aursearch.ApiResponse
import com.aliucord.entities.MessageEmbedBuilder
import com.discord.api.message.embed.MessageEmbed
import com.aliucord.Http
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.Plugin
import com.discord.models.commands.ApplicationCommandOption
import java.io.IOException
import java.util.ArrayList

@AliucordPlugin
class AURSearch : Plugin() {
    override fun start(context: Context) {
        val arguments = ArrayList<ApplicationCommandOption>()
        arguments.add(createCommandOption(ApplicationCommandType.STRING, "query", "What to search for.", null, true, true))
        commands.registerCommand(
                "aur",
                "Searches the AUR.",
                arguments
        ) { ctx: CommandContext ->
            val query = ctx.getRequiredString("query")
            var res: ApiResponse?
            var embed: MessageEmbedBuilder?
            var finishedEmbed: MessageEmbed? = null
            var i = 0
            var result: String
            try {
                val baseURL = "https://aur.archlinux.org/rpc/?v=5&type=search&arg="
                val aurURL = "https://aur.archlinux.org/packages/"
                res = Http.simpleJsonGet(baseURL + query, ApiResponse::class.java)
                result = "I found the following:"
                embed = MessageEmbedBuilder()
                        .setColor(2266867)
                        .setTitle("AUR Search")
                        .setDescription("For all results, click [here]($aurURL?K=$query)")
                        .setFooter(res.resultcount.toString() + " results found, showing first 5.", "https://www.clipartmax.com/png/full/321-3216004_by-default-most-linux-distributions-including-arch-arch-linux-logo-png.png")
                for (pkg in res.results!!) {
                    if (i < 5) {
                        embed.addField(pkg.Name, """${pkg.Description}
${String.format("[Link](%s)", aurURL + pkg.Name)}""", false)
                        i++
                    } else {
                        break
                    }
                }
                finishedEmbed = embed.build()
            } catch (t: IOException) {
                result = "Something went wrong: " + t.message
            }
            CommandResult(result, listOf(finishedEmbed), false)
        }
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
}