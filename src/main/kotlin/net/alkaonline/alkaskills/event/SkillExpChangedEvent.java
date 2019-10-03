package net.alkaonline.alkaskills.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

//도저히 코틀린을 몰라서 이건 자바로 만듬...
public class SkillExpChangedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private Player player;

    private boolean cancelled = false;

    public SkillExpChangedEvent(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
