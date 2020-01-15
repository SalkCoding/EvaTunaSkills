package net.alkaonline.alkaskills.skilltree.utility

import me.finalchild.kotlinbukkit.util.times
import net.alkaonline.alkaskills.Skill
import net.alkaonline.alkaskills.SkillCategory
import org.bukkit.ChatColor
import org.bukkit.Material

val doubleJump = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}이중 도약", SkillCategory.UTILITY,
        listOf("공중에서 앉기 키를 누를 시 한번 더 도약 할 수 있습니다.",
                "${ChatColor.YELLOW}레벨이 올라갈 시 2분, 1분, 30초로",
                "${ChatColor.YELLOW}쿨타임이 감소합니다."), 3, Material.FIREWORK_ROCKET * 1
)