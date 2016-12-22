package me.realized.duels.commands.other;

import me.realized.duels.commands.BaseCommand;
import me.realized.duels.utilities.Helper;

import org.bukkit.entity.Player;

public class QueueCommand extends BaseCommand {
    public QueueCommand() {
        super("queue", "duels.queue");
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length == 0 || (!args[0].equalsIgnoreCase("join") && !args[0].equalsIgnoreCase("leave"))) {
            Helper.pm(sender, "Commands.queue.usage", true);
            return;
        }

        if (args[0].equalsIgnoreCase("join")) {
            if (queueManager.addToQueue(sender)) {
                int position = queueManager.getPosition(sender);

                Helper.pm(sender, "Queueing.added-to-queue", true, "{POSITION}", position, "{SIZE}", queueManager.getQueueSize());
            } else {
                Helper.pm(sender, "Queueing.already-in-queue", true);
            }
        } else {
            if (queueManager.removeFromQueue(sender)) {
                Helper.pm(sender, "Queueing.removed-from-queue", true);
            } else {
                Helper.pm(sender, "Queueing.not-already-in-queue", true);
            }
        }
    }
}
