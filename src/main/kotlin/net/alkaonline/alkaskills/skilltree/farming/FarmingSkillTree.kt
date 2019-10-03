package net.alkaonline.alkaskills.skilltree.farming

import me.finalchild.kotlinbukkit.util.times
import net.alkaonline.alkaskills.*
import org.bukkit.ChatColor
import org.bukkit.Material

val t1Wheat = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}밀 농사",
    SkillCategory.FARMING,
    listOf("밀 농사를 통하여 경험치를 얻을 수 있습니다.", "${ChatColor.RED}달콤한 열매 수확과 동시에 선택할 수 없습니다."),
    3,
    Material.WHEAT * 1,
    ExclusiveRequirement("달콤한 열매 수확")
)
val t1SweetBerry = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}달콤한 열매 수확",
    SkillCategory.FARMING,
    listOf("달콤한 열매 수확을 통하여 경험치를 얻을 수 있습니다.", "${ChatColor.RED}밀 농사와 동시에 선택할 수 없습니다."),
    3,
    Material.SWEET_BERRIES * 1,
    ExclusiveRequirement(t1Wheat)
)

val t2Potato = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}감자 농사", SkillCategory.FARMING,
    listOf(
        "감자 농사를 통하여 경험치를 얻을 수 있습니다.",
        "${ChatColor.RED}당근 농사와 동시에 선택할 수 없습니다.",
        "${ChatColor.RED}밀 농사 또는 달콤한 열매 수확을 먼저 배워야 합니다."
    ), 3, Material.POTATO * 1, SkillOrRequirement(t1Wheat, t1SweetBerry), ExclusiveRequirement("당근 농사")
)
val t2Carrot = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}당근 농사", SkillCategory.FARMING,
    listOf(
        "당근 농사를 통하여 경험치를 얻을 수 있습니다.",
        "${ChatColor.RED}감자 농사와 동시에 선택할 수 없습니다.",
        "${ChatColor.RED}밀 농사 또는 달콤한 열매 수확을 먼저 배워야 합니다."
    ), 3, Material.CARROT * 1, SkillOrRequirement(t1Wheat, t1SweetBerry), ExclusiveRequirement(t2Potato)
)

val t3NetherWart = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}네더 사마귀 농사",
    SkillCategory.FARMING,
    listOf("네더 사마귀 농사를 통하여 경험치를 얻을 수 있습니다.", "${ChatColor.RED}감자 또는 당근 농사를 먼저 배워야 합니다."),
    3,
    Material.NETHER_WART * 1,
    SkillOrRequirement(t2Carrot, t2Potato)
)

val t3Pumpkin = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}호박 농사",
    SkillCategory.FARMING,
    listOf("호박 농사를 통하여 경험치를 얻을 수 있습니다.", "${ChatColor.RED}감자 또는 당근 농사를 먼저 배워야 합니다."),
    3,
    Material.PUMPKIN * 1,
    SkillOrRequirement(t2Carrot, t2Potato)
)

val t3Melon = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}수박 농사",
    SkillCategory.FARMING,
    listOf("수박 농사를 통하여 경험치를 얻을 수 있습니다.", "${ChatColor.RED}감자 또는 당근 농사를 먼저 배워야 합니다."),
    3,
    Material.MELON * 1,
    SkillOrRequirement(t2Carrot, t2Potato)
)

val t4SugarCane = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}사탕수수 농사",
    SkillCategory.FARMING,
    listOf(
        "사탕수수 농사를 통하여 경험치를 얻을 수 있습니다.",
        "${ChatColor.RED}선인장 농사와 동시에 선택할 수 없습니다.",
        "${ChatColor.RED}네더 사마귀, 호박, 수박 농사 중 한가지를 먼저 배워야 합니다."
    ),
    3,
    Material.SUGAR_CANE * 1,
    SkillOrRequirement(t3NetherWart, t3Melon, t3Pumpkin),
    ExclusiveRequirement("선인장 농사")
)
val t4Cactus = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}선인장 농사",
    SkillCategory.FARMING,
    listOf(
        "선인장 농사를 통하여 경험치를 얻을 수 있습니다.",
        "${ChatColor.RED}사탕수수 농사와 동시에 선택할 수 없습니다.",
        "${ChatColor.RED}네더 사마귀, 호박, 수박 농사 중 한가지를 먼저 배워야 합니다."
    ),
    3,
    Material.CACTUS * 1,
    SkillOrRequirement(t3NetherWart, t3Melon, t3Pumpkin),
    ExclusiveRequirement(t4SugarCane)
)

val t5Beetroot = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}사탕무 농사", SkillCategory.FARMING,
    listOf(
        "사탕무 농사를 통하여 경험치를 얻을 수 있습니다.",
        "${ChatColor.RED}코코아 농사와 동시에 선택할 수 없습니다.",
        "${ChatColor.RED}사탕수수 또는 선인장 농사를 먼저 배워야 합니다."
    ), 3, Material.BEETROOT * 1, SkillOrRequirement(t4SugarCane, t4Cactus), ExclusiveRequirement("코코아 농사")
)
val t5Cocoa = Skill(
    "${ChatColor.GREEN}${ChatColor.BOLD}코코아 농사", SkillCategory.FARMING,
    listOf(
        "코코아 농사를 통하여 경험치를 얻을 수 있습니다.",
        "${ChatColor.RED}사탕무 농사와 동시에 선택할 수 없습니다.",
        "${ChatColor.RED}사탕수수 또는 선인장 농사를 먼저 배워야 합니다."
    ), 3, Material.COCOA_BEANS * 1, SkillOrRequirement(t4SugarCane, t4Cactus), ExclusiveRequirement(t5Beetroot)
)
