package net.alkaonline.alkaskills.skilltree.logging

import me.finalchild.kotlinbukkit.util.times
import net.alkaonline.alkaskills.Skill
import net.alkaonline.alkaskills.SkillCategory
import org.bukkit.ChatColor
import org.bukkit.Material

val canAlkaExp = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}경험치 획득", SkillCategory.LOGGING,
        listOf("벌목시 경험치를 획득할 수 있습니다."), 3, Material.EXPERIENCE_BOTTLE * 1
)

val chopTree = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}찹 트리", SkillCategory.LOGGING,
        listOf("확률적으로 나무가 한번에 베어집니다."), 3, Material.OAK_SAPLING * 1
)