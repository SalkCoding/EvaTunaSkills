package net.alkaonline.alkaskills.skilltree.mining

import me.finalchild.kotlinbukkit.util.times
import net.alkaonline.alkaskills.Skill
import net.alkaonline.alkaskills.SkillCategory
import org.bukkit.ChatColor
import org.bukkit.Material

val moreMin = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}광물 개수 증가", SkillCategory.MINING,
        listOf("채광할 때 나오는 광물의 개수가 증가합니다.",
                "${ChatColor.RED}10 레벨 마다 스킬레벨을 올릴 수 있습니다.",
                "${ChatColor.RED}시작 레벨이 1이므로 레벨 11부터 올릴 수 있습니다."), 3, Material.IRON_PICKAXE * 1
)

val betterIron = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}철 채광 숙련", SkillCategory.MINING,
        listOf("더 좋은 등급의 철을 얻을 수 있게 됩니다."), 3, Material.IRON_INGOT * 1
)

val betterDiamond = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}다이아몬드 채광 숙련", SkillCategory.MINING,
        listOf("더 좋은 등급의 다이아몬드를 얻을 수 있게 됩니다."), 3, Material.DIAMOND * 1
)

val fasterMin = Skill(
        "${ChatColor.GRAY}${ChatColor.BOLD}채광 속도 증가", SkillCategory.MINING,
        listOf("한 번에 블록에 가하는 피해가 커집니다."), 5, Material.DIAMOND_PICKAXE * 1
)
