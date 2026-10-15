package com.github.deutschich.modpackupnotify.ModPackUpNotify;

import net.fabricmc.api.ModInitializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.nio.file.Path;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ModPackUpNotify");
    public void onInitialize() {
        LOGGER.error("Sorry, but this Mod is only for client-side, (I made this mod in server side, and it couldn't import \"net.minecraft.client.Minecraft\", and because of this I lost 2 hours :(");
    }
}
