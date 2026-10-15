package com.github.deutschich.modpackupnotify.ModPackUpNotify.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.chat.Component; // To send this notification to update your Modpack (eg. BadPCMC)

import net.minecraft.client.Minecraft;

public class mainClient implements ClientModInitializer {


    HttpClient webclient = HttpClient.newHttpClient();

    private String mpupntfyDownloadUrl;
    private String mpupntfyCurrentVersion;
    private String mpupntfyCurrentVersionMSG1Part;
    private String mpupntfyCurrentVersionMSG2Part;
    private Boolean updateAvailable;
    private String mpupntfyversion;
    private String mpupntfyWhereGetUpdate;
    private String modrinthId;
    private static final Path MPUPNTFYCONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("ModPackUpNotify.json");


    @Override
    public void onInitializeClient() {
        getTheConfigForThisMod();
        if (!mpupntfyWhereGetUpdate.equals("url") && !mpupntfyWhereGetUpdate.equals("modrinth")) {
            throw new IllegalArgumentException("Please set the Path in the Config at \"update-location\"! It's now set to '" + mpupntfyWhereGetUpdate + ". Please change it!");
        }
        checkForTheUpdatesFunc();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (updateAvailable) {
                mpupntfyUpdateNtfy();
            }
        });
    }


    private void getTheConfigForThisMod() { // Why did I name this function so long?


        try {

            if (!Files.exists(MPUPNTFYCONFIG_PATH) || Files.readString(MPUPNTFYCONFIG_PATH).isBlank()) {
                Files.writeString(MPUPNTFYCONFIG_PATH, "{\n \"download-url\": \"https://raw.githubusercontent.com/deutschich/example-modpackupnotify/main/update.json\",\n  \"current-version\": \"1.0.0-example\",\n  \"msg-first-part\": \"§aA new Update for §lModpackName§r§a is available!\",\n  \"msg-second-part\": \"Update now for newest features/newest MC Version!\",\n \"update-location\": \"url\",\n \"modrinth-id\": \"Ca5KfBLs\"}");
            }
            String mpupntfyconfigContent = Files.readString(MPUPNTFYCONFIG_PATH);
            JsonObject mpupntfyconfig = JsonParser.parseString(mpupntfyconfigContent).getAsJsonObject();
            mpupntfyDownloadUrl = mpupntfyconfig.get("download-url").getAsString();
            mpupntfyCurrentVersion = mpupntfyconfig.get("current-version").getAsString();
            mpupntfyCurrentVersionMSG1Part = mpupntfyconfig.get("msg-first-part").getAsString();
            mpupntfyCurrentVersionMSG2Part = mpupntfyconfig.get("msg-second-part").getAsString();
            mpupntfyWhereGetUpdate = mpupntfyconfig.get("update-location").getAsString();
            modrinthId = mpupntfyconfig.get("modrinth-id").getAsString();

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void checkForTheUpdatesFunc() { // Why did I name this function so long?
        try {
            String mpupntfyDownloadUrlIncode = "https://raw.githubusercontent.com/deutschich/example-modpackupnotify/main/update.json";
            String modrinthIdIncode;
            if (mpupntfyWhereGetUpdate.equals("url")) {
                mpupntfyDownloadUrlIncode = mpupntfyDownloadUrl;
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(mpupntfyDownloadUrlIncode)).header("User-Agent", "ModpackUpNotify").GET().timeout(Duration.ofSeconds(5)).build();
                HttpResponse<String> response = webclient.send(request, HttpResponse.BodyHandlers.ofString());
                String jsonContent = response.body();
                JsonObject json = JsonParser.parseString(jsonContent).getAsJsonObject();
                mpupntfyversion = json.get("newest-version").getAsString();
            }
            if (mpupntfyWhereGetUpdate.equals("modrinth")) {
                modrinthIdIncode = modrinthId;
                mpupntfyDownloadUrlIncode = "https://api.modrinth.com/v2/project/" + modrinthIdIncode + "/version";
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(mpupntfyDownloadUrlIncode)).header("User-Agent", "ModpackUpNotify").GET().timeout(Duration.ofSeconds(5)).build();
                HttpResponse<String> response = webclient.send(request, HttpResponse.BodyHandlers.ofString());
                String jsonContent = response.body();
                JsonArray versions = JsonParser.parseString(jsonContent).getAsJsonArray();
                List<JsonObject> list = new ArrayList<>();
                for (JsonElement e : versions) list.add(e.getAsJsonObject());
                list.sort((a, b) -> b.get("date_published").getAsString()
                        .compareTo(a.get("date_published").getAsString()));
                JsonObject newest = list.get(0);
                mpupntfyversion = newest.get("version_number").getAsString();
            }
            updateAvailable = !mpupntfyversion.equals(mpupntfyCurrentVersion);

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void mpupntfyUpdateNtfy() {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("§a§lModpackUpNotify§r§8 - §7" + mpupntfyCurrentVersionMSG1Part));
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("§a§lModpackUpNotify§r§8 - §7" + mpupntfyCurrentVersion + " > " + mpupntfyversion));
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("§a§lModpackUpNotify§r§8 - §7" + mpupntfyCurrentVersionMSG2Part));
        }
    }
}
