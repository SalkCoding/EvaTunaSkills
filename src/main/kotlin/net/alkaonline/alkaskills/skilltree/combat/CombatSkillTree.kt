package net.alkaonline.alkaskills.skilltree.combat

import me.finalchild.kotlinbukkit.util.times
import net.alkaonline.alkaskills.Skill
import net.alkaonline.alkaskills.SkillCategory
import org.bukkit.ChatColor
import org.bukkit.Material

val swordM = Skill(
        "${ChatColor.GOLD}${ChatColor.BOLD}다중 타격", SkillCategory.COMBAT,
        listOf("${ChatColor.LIGHT_PURPLE}검으로 공격시 추가적으로 대상을 벱니다.",
                "${ChatColor.LIGHT_PURPLE}스킬 레벨이 높아질수록 베는 횟수가 증가합니다.",
                "${ChatColor.RED}채광을 통해 얻은 광물로(철, 다이아) 만든 검을 사용시에만 작동합니다."), 3, Material.GOLDEN_SWORD * 1
)

val axeM = Skill(
        "${ChatColor.GOLD}${ChatColor.BOLD}강타", SkillCategory.COMBAT,
        listOf("${ChatColor.LIGHT_PURPLE}도끼로 공격시 일정확률로 대상에게 폭발을 일으킵니다.",
                "${ChatColor.LIGHT_PURPLE}스킬 레벨이 높아질수록 폭발확률이 증가합니다.",
                "${ChatColor.RED}채광을 통해 얻은 광물로(철, 다이아) 만든 도끼를 사용시에만 작동합니다."), 4, Material.GOLDEN_AXE * 1
)

val bowM = Skill(
        "${ChatColor.GOLD}${ChatColor.BOLD}다중 화살", SkillCategory.COMBAT,
        listOf("${ChatColor.LIGHT_PURPLE}일정 확률로 여러 발의 화살이 동시에 발사됩니다.",
                "${ChatColor.LIGHT_PURPLE}스킬 레벨이 높아질수록 화살개수와 확률이 증가합니다."), 3, Material.BOW * 1
)

val bodyM = Skill(
        "${ChatColor.GOLD}${ChatColor.BOLD}신체 단련", SkillCategory.COMBAT,
        listOf("${ChatColor.LIGHT_PURPLE}스킬 레벨에 비례하여 영구적으로 체력이 증가합니다.",
                "${ChatColor.RED}5 레벨 마다 스킬레벨을 올릴 수 있습니다.",
                "${ChatColor.RED}시작 레벨이 1이므로 레벨 6부터 올릴 수 있습니다."), 5, Material.GOLDEN_CHESTPLATE * 1
)
