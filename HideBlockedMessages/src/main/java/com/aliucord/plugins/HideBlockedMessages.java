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
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aliucord.Logger;
import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.aliucord.patcher.PinePrePatchFn;
import com.aliucord.utils.ReflectUtils;
import com.discord.databinding.WidgetChatListAdapterItemBlockedBinding;
import com.discord.models.member.GuildMember;
import com.discord.stores.StoreStream;
import com.discord.stores.StoreUserRelationships;
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemBlocked;
import com.discord.widgets.chat.list.entries.ChatListEntry;
import com.discord.widgets.chat.overlay.ChatTypingModel$Companion$getTypingUsers$1$1;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class HideBlockedMessages extends Plugin {
    private final Logger logger = new Logger("HideBlockedMessages");
    @NonNull
    @Override
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Alyxia", 465702500146610176L), new Manifest.Author("zt", 289556910426816513L) };
        manifest.description = "Completely hides blocked messages.";
        manifest.version = "1.2.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/lexisother/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void start(Context context) throws NoSuchMethodException {
        patcher.patch(WidgetChatListAdapterItemBlocked.class.getDeclaredMethod("onConfigure", int.class, ChatListEntry.class), new PinePatchFn(callFrame -> {
            try {
                View root = ((WidgetChatListAdapterItemBlockedBinding) Objects.requireNonNull(ReflectUtils.getField(callFrame.thisObject, "binding"))).getRoot();
                root.setVisibility(View.GONE);
                root.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                logger.error(e);
            }
        }));

        final StoreUserRelationships storeUserRelationships = StoreStream.getUserRelationships();
        patcher.patch(ChatTypingModel$Companion$getTypingUsers$1$1.class.getDeclaredMethod("call", Map.class, Map.class), new PinePrePatchFn(callFrame -> {
            callFrame.args[1] = ((Map<Long, GuildMember>) callFrame.args[1]).entrySet().stream()
                    .filter(entry -> Objects.requireNonNull(storeUserRelationships.getRelationships().get(entry.getKey())) != 2)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }));
    }

    @Override
    public void stop(Context context) { patcher.unpatchAll(); }
}