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

import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.discord.databinding.WidgetChatListAdapterItemBlockedBinding;
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemBlocked;
import com.discord.widgets.chat.list.entries.ChatListEntry;

import java.lang.reflect.Field;


@SuppressWarnings("unused")
public class HideBlockedMessages extends Plugin {
    @NonNull
    @Override
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[] { new Manifest.Author("Alyxia", 465702500146610176L) };
        manifest.description = "Completely hides blocked messages.";
        manifest.version = "1.1.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/lexisother/AliucordPlugins/builds/updater.json";
        return manifest;
    }

    @Override
    public void start(Context context) throws Throwable {
        Field bindingField = WidgetChatListAdapterItemBlocked.class.getDeclaredField("binding");
        bindingField.setAccessible(true);

        patcher.patch(WidgetChatListAdapterItemBlocked.class.getDeclaredMethod("onConfigure", int.class, ChatListEntry.class), new PinePatchFn(callFrame -> {
            try {
                WidgetChatListAdapterItemBlockedBinding binding = (WidgetChatListAdapterItemBlockedBinding) bindingField.get(callFrame.thisObject);
                if (binding == null) return;
                binding.getRoot().setVisibility(View.GONE);
                binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }));

    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
    }
}
