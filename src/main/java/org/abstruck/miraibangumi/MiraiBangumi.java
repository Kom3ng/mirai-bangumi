package org.abstruck.miraibangumi;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import org.abstruck.miraibangumi.command.CalendarCommand;
import org.abstruck.miraibangumi.command.CharacterDetailCommand;
import org.abstruck.miraibangumi.command.PersonDetailCommand;
import org.abstruck.miraibangumi.command.SearchResultNextPageCommand;
import org.abstruck.miraibangumi.command.SearchSubjectCommand;
import org.abstruck.miraibangumi.command.SubjectDetailCommand;

public final class MiraiBangumi extends JavaPlugin {
    public static final MiraiBangumi INSTANCE = new MiraiBangumi();

    private MiraiBangumi() {
        super(new JvmPluginDescriptionBuilder("org.abstruck.mirai-bangumi", "0.1.0")
                .name("mirai-bangumi")
                .author("Astrack")
                .build());
    }

    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(new CalendarCommand(), true);
        CommandManager.INSTANCE.registerCommand(new SearchSubjectCommand(), true);
        CommandManager.INSTANCE.registerCommand(new SearchResultNextPageCommand(), true);
        CommandManager.INSTANCE.registerCommand(new SubjectDetailCommand(), true);
        CommandManager.INSTANCE.registerCommand(new PersonDetailCommand(), true);
        CommandManager.INSTANCE.registerCommand(new CharacterDetailCommand(), true);
        getLogger().info("Plugin loaded!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        CommandManager.INSTANCE.unregisterAllCommands(this);
    }
}