package me.realized.duels.dueling;

import me.realized.duels.Core;
import me.realized.duels.hooks.CombatTagPlusHook;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QueueManager implements Listener, Runnable {

    private final Core instance;
    private final List<UUID> queuedPlayers;

    public QueueManager(Core instance) {
        this.instance = instance;
        this.queuedPlayers = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    public boolean addToQueue(Player player) {
        if (queuedPlayers.contains(player.getUniqueId())) {
            return false;
        }
        queuedPlayers.add(player.getUniqueId());
        return true;
    }

    public boolean removeFromQueue(Player player) {
        return queuedPlayers.remove(player.getUniqueId());
    }

    public Integer getPosition(Player player) {
        return queuedPlayers.indexOf(player.getUniqueId());
    }

    public int getQueueSize() {
        return queuedPlayers.size();
    }

    @Override
    public void run() {
        while (queuedPlayers.size() >= 2) {
            if (instance.getArenaManager().getAvailableArena() == null) {
                return;
            }

            // Try to start a match
            Player player1 = instance.getServer().getPlayer(queuedPlayers.remove(0));
            if (checkPlayer(player1)) {
                continue;
            }

            Player player2 = instance.getServer().getPlayer(queuedPlayers.remove(0));
            if (checkPlayer(player2)) {
                queuedPlayers.add(0, player1.getUniqueId());
                continue;
            }

            Request request = new Request(player1.getUniqueId(), player2.getUniqueId(), new Settings(player2.getUniqueId(), null));
            instance.getDuelManager().startMatch(player1, player2, request);
        }
    }

    private boolean checkPlayer(Player player) {
        if (player == null) {
            return true;
        }

        if (instance.getArenaManager().isInMatch(player)) {
            return true;
        }

        if (player.getGameMode() == GameMode.CREATIVE && instance.getConfiguration().isPatchesDisallowCreativeDueling()) {
            return true;
        }

        CombatTagPlusHook ctph = (CombatTagPlusHook) instance.getHookManager().get("CombatTagPlus");
        if (ctph.isTagged(player)) {
            return true;
        }

        if (instance.getArenaManager().isInMatch(player)) {
            return true;
        }

        if (player.isDead()) {
            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        queuedPlayers.remove(event.getPlayer().getUniqueId());
    }

}
