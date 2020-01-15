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

val loggingFortune = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}벌목 행운", SkillCategory.LOGGING,
        listOf("벌목시 일정 확률로 이동 속도가 증가합니다."), 3, Material.FEATHER * 1
)

val chopTree = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}찹 트리", SkillCategory.LOGGING,
        listOf("확률적으로 나무가 한번에 베어집니다.",
                "${ChatColor.YELLOW}(정글 나무 원목에는 적용되지 않습니다.)"), 4, Material.OAK_SAPLING * 1
)