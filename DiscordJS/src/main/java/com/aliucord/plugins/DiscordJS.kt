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
import com.discord.api.message.embed.MessageEmbed
import com.aliucord.Http
import com.aliucord.Logger
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.Plugin
import com.discord.models.commands.ApplicationCommandOption
import java.util.ArrayList

@AliucordPlugin
class DiscordJS : Plugin() {
    private val logger = Logger("DiscordJS")

    override fun start(context: Context) {
        val arguments = ArrayList<ApplicationCommandOption>()
        arguments.add(createCommandOption(ApplicationCommandType.STRING, "query", "What to search for.", null, true))
        commands.registerCommand(
                "discordjs",
                "Searches the Discord.JS docs.",
                arguments
        ) { ctx: CommandContext ->
            val query = ctx.getRequiredString("query")
            var res: MessageEmbed? = null
            var result: String
            try {
                res = Http.simpleJsonGet("https://djsdocs.sorta.moe/v2/embed?src=master&q=$query", MessageEmbed::class.java)
                result = "I found the following:"
            } catch (t: Throwable) {
                logger.error(t)
                result = "Something went wrong: " + t.message
            }
            CommandResult(result, listOf(res), false)
        }
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
}