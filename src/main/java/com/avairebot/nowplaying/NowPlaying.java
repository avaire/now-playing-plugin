package com.avairebot.nowplaying;

import com.avairebot.plugin.JavaPlugin;

public class NowPlaying extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        registerEventListener(new MusicEventListener(this));
    }

    /**
     * Checks if the given guild ID is in the server IDs list from the config.
     *
     * @param guildId The guild ID that should be checked.
     * @return <code>True</code> if the guild ID is in the server IDs
     * list from the config, <code>False</code> otherwise.
     */
    boolean isValidId(long guildId) {
        return getConfig().getStringList("server-ids")
            .contains(String.valueOf(guildId));
    }
}
