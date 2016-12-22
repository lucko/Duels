package me.realized.duels.commands;

import me.realized.duels.Core;
import me.realized.duels.arena.ArenaManager;
import me.realized.duels.configuration.ConfigType;
import me.realized.duels.configuration.MainConfig;
import me.realized.duels.configuration.MessagesConfig;
import me.realized.duels.data.DataManager;
import me.realized.duels.dueling.QueueManager;
import me.realized.duels.dueling.RequestManager;
import me.realized.duels.dueling.SpectatorManager;
import me.realized.duels.hooks.HookManager;
import me.realized.duels.kits.KitManager;
import me.realized.duels.utilities.Helper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand implements CommandExecutor {

    private final String command;
    private final String permission;

    protected final MainConfig config = Core.getInstance().getConfiguration();
    protected final MessagesConfig messages = (MessagesConfig) Core.getInstance().getConfigManager().getConfigByType(ConfigType.MESSAGES);
    protected final RequestManager requestManager = Core.getInstance().getRequestManager();
    protected final DataManager dataManager = Core.getInstance().getDataManager();
    protected final ArenaManager arenaManager = Core.getInstance().getArenaManager();
    protected final SpectatorManager spectatorManager = Core.getInstance().getSpectatorManager();
    protected final KitManager kitManager = Core.getInstance().getKitManager();
    protected final QueueManager queueManager = Core.getInstance().getQueueManager();
    protected final HookManager hookManager = Core.getInstance().getHookManager();

    protected BaseCommand(String command, String permission) {
        this.command = command;
        this.permission = permission;
    }

    public String getName() {
        return command;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Helper.pm(sender, "&cThis command cannot be ran by " + sender.getName() + ".", false);
            return true;
        }

        if (!sender.hasPermission(permission)) {
            Helper.pm(sender, "Errors.no-permission", true);
            return true;
        }

        execute((Player) sender, args);
        return true;
    }

    public abstract void execute(Player sender, String[] args);
}
