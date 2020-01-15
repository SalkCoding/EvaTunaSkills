package net.alkaonline.alkaskills.skilltree.fishing

import me.finalchild.kotlinbukkit.util.times
import net.alkaonline.alkaskills.Skill
import net.alkaonline.alkaskills.SkillCategory
import org.bukkit.ChatColor
import org.bukkit.Material

val codAlkaExp = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}대구", SkillCategory.FISHING,
        listOf("대구를 낚을시 경험치를 얻습니다."), 4, Material.COD * 1
)

val tropicalFishAlkaExp = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}열대어", SkillCategory.FISHING,
        listOf("열대어를 낚을시 경험치를 얻습니다."), 4, Material.TROPICAL_FISH * 1
)

val salmonAlkaExp = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}연어", SkillCategory.FISHING,
        listOf("연어를 낚을시 경험치를 얻습니다."), 4, Material.SALMON * 1
)

val pufferFishAlkaExp = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}복어", SkillCategory.FISHING,
        listOf("복어를 낚을시 경험치를 얻습니다."), 4, Material.PUFFERFISH * 1
)

val fishingFortune = Skill("${ChatColor.GRAY}${ChatColor.BOLD}낚시 행운", SkillCategory.FISHING,
        listOf("낚시 성공시 확률적으로 추가적인 보상을 얻습니다."), 3, Material.FISHING_ROD * 1
)
