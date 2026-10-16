# ModPackUpNotify

## About
ModPackUpNotify is an [Open Source](https://github.com/deutschich/ModPackUpNofity) easy-to-use Minecraft Mod for Modpack Developers to notify users of the Modpack if a newer version is available. It is very easy to configure and very easy to implement in Modpacks hosted on Modrinth. For Modpacks that aren't hosted on Modrinth, you have to host a public accessible json File (It's possible to host it on a GitHub Repository)


## Configuration
This mod is configurable by a file located in `{path-to-your-mc-dir}/config/ModPackUpNotify.json`. Default Content:

```json
{
 "download-url": "https://raw.githubusercontent.com/deutschich/example-modpackupnotify/main/update.json",
 "current-version": "1.0.0-example",
 "msg-first-part": "§aA new Update for §lModpackName§r§a is available!",
 "msg-second-part": "Update now for newest features/newest MC Version!",
 "update-location": "url",
 "modrinth-id": "Ca5KfBLs"
}
```
(The default Config is connected with my GitHub Repo with an example structure of such a repo to prevent errors because of an missing URL, the formatting is in the automatic created Config looks a little bit off but it doesn't affects the Mod)

### Configuration Entries:

|Entry|Meaning|Default value|Allowed values|
|---|---|---|---|
|`download-url`|The URL with the version file, works only in `url` mode, works only with direct json files in the output|`https://raw.githubusercontent.com/deutschich/example-modpackupnotify/main/update.json`|Every URL that leads directly to a JSON File|
|`current-version`|The version of the modpack that is currently installed|`1.0.0-example`|Every version Number the Modpack has is possible, but it should have the same name as on Modrinth or in the manual JSON File|
|`msg-first-part`|First Part of the Message if an update is available|`§aA new Update for §lModpackName§r§a is available!`|Every Message, but colors have to be defined with `§` Symboles and not with `&` as Minecraft vanilla colors use `§` instead of the `&` symbol commonly found in Paper/Spigot.|
|`msg-second-part`|Same as the First Part, but the second part|`Update now for newest features/newest MC Version!`|Same as the First Part, but the second part|
|`update-location`|Use this, to set if you want to get the information for new updates from modrinth or from an URL with a JSON File|`url`|`url` or `modrinth`|
|`modrinth-id`|The ID of the Modrinth Project, if the mod is in Modrinth-Mode, default ID is the ID of my Modpack BadPC-MC to avoid setting an invalid ID in the default Config|`Ca5KfBLs`|Every Modrinth Project ID|
