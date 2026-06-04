# Re LPChatPrefix

This is a simple Server-Sided mod that gives you LuckPerms prefix/suffix support for usernames 
in your chat messages and join/leave messages with Adventure's MiniMessage support in NeoForge

## Requirements

- NeoForge 1.21.1
- [Luckperms 5.4](https://modrinth.com/mod/luckperms)

### Included Mods

The following mods are bundled (Jar-in-Jar) with this mod and do not need to be installed separately:

- [Adventure Platform Mod 6.0.0](https://modrinth.com/mod/adventure-platform-mod)

## Config

The config is located at `./config/relpchatprefix/config.json` and by default will look like the following:

```json
{
  "mainConfig": {
    "enablePrefix": true,
    "enableSuffix": true,
    "messageFormat": "{prefix}{player}{suffix} <gray><bold>»<reset>",
    "messageColor": "white"
  },
  "chatOverrides": {
    "joinMessage": "<yellow>{prefix}{player}{suffix} joined the game<reset>",
    "leaveMessage": "<yellow>{prefix}{player}{suffix} left the game<reset>"
  },
  "firstJoin": {
    "message": "<yellow>Welcome {player} to the server!",
    "enable": false
  }
}
```

### Main Config options

#### `enablePrefix`

Allows you to enable or disable prefixes in the chat

#### `enableSuffix`

Allows you to enable or disable Suffixes in the chat

#### `messageFormat`

The provided format for chat messages being sent by users.

**Supported Placeholders:**
- `{prefix}`: The player's LuckPerms prefix
- `{player}`: The player's username
- `{suffix}`: The player's LuckPerms suffix

You can use any MiniMessage supported text code for the message format. 
For more details check out the [Adventure MiniMessage Docs](https://docs.papermc.io/adventure/minimessage/format/)

#### `messageColor`

The provided message color to use for chat messages, by default uses White to align with the Minecraft defaults.

You can use any standard Minecraft formatting color name.

### Chat Overrides

**Supported Placeholders:**
- `{prefix}`: The player's LuckPerms prefix
- `{player}`: The player's username
- `{suffix}`: The player's LuckPerms suffix

You can use any MiniMessage supported text code for the message format.
For more details check out the [Adventure MiniMessage Docs](https://docs.papermc.io/adventure/minimessage/format/)

#### `joinMessage`

This option allows you to override the default join message from Minecraft

#### `leaveMessage`

This option allows you to override the default leave message from Minecraft

### First Join

**Supported Placeholders:**
- `{prefix}`: The player's LuckPerms prefix
- `{player}`: The player's username
- `{suffix}`: The player's LuckPerms suffix

You can use any MiniMessage supported text code for the message format.
For more details check out the [Adventure MiniMessage Docs](https://docs.papermc.io/adventure/minimessage/format/)

#### `message`

Allows setting the first join message for players

#### `enable`

Toggle for First join message