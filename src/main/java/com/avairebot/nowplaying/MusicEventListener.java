package com.avairebot.nowplaying;

import com.avairebot.contracts.handlers.EventListener;
import com.avairebot.handlers.events.MusicEndedEvent;
import com.avairebot.handlers.events.NowPlayingEvent;
import com.avairebot.scheduler.tasks.ChangeGameTask;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class MusicEventListener extends EventListener {

    private static final Logger log = LoggerFactory.getLogger(MusicEventListener.class);

    private final NowPlaying plugin;
    private final Set<Long> playingMusic;

    /**
     * Creates the new music event listener.
     *
     * @param plugin The now playing plugin instance.
     */
    MusicEventListener(NowPlaying plugin) {
        this.plugin = plugin;
        this.playingMusic = new HashSet<>();
    }

    @Override
    public void onNowPlaying(NowPlayingEvent event) {
        if (!plugin.isValidId(event.getGuildId())) {
            return;
        }

        log.debug("{} (ID: {}) is now playing \"{}\", changing status to song title.",
            event.getGuild().getName(), event.getGuildId(), event.getSongTitle()
        );

        ChangeGameTask.hasCustomStatus = true;
        plugin.getAvaire().getShardManager().setActivity(
            Activity.listening(event.getSongTitle())
        );
    }

    @Override
    public void onMusicEnded(MusicEndedEvent event) {
        playingMusic.remove(event.getGuildId());

        if (ChangeGameTask.hasCustomStatus && playingMusic.isEmpty()) {
            log.debug("{} (ID: {}) has stopped playing music, resuming dynamic status message.",
                event.getGuild().getName(), event.getGuildId()
            );

            ChangeGameTask.hasCustomStatus = false;
        }
    }
}
