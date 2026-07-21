package it.globalx.bungee.module;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.bungee.GlobalXBungee;
import lombok.Data;

@Data
public abstract class Module {
    private final String name;
    private final String description;
    private final String configPath;

    public void enable(Section section) {
        if (!section.getBoolean("Enabled", true)) {
            return;
        }

        onEnable(section);
    }

    protected abstract void onEnable(Section section);

    public abstract void onDisable();

    public GlobalXBungee plugin() {
        return GlobalXBungee.getInstance();
    }

}
