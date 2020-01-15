package net.alkaonline.alkaskills.skillswitch.childswitch

import net.alkaonline.alkaskills.getPlayerSkillPoint
import net.alkaonline.alkaskills.skillswitch.SwitchManager
import net.alkaonline.alkaskills.skilltree.logging.chopTree
import java.nio.file.Path
import java.util.*

class ChopTreeSwitchManager(chopTreeDirectory: Path) : SwitchManager(chopTreeDirectory) {

    override fun saveAndUnloadData(id: UUID) {
        if (id.getPlayerSkillPoint(chopTree) > 0)
            super.saveAndUnloadData(id)
    }

    override fun saveData(id: UUID) {
        if (id.getPlayerSkillPoint(chopTree) > 0)
            super.saveData(id)
    }

    override fun loadData(id: UUID) {
        if (id.getPlayerSkillPoint(chopTree) > 0)
            super.loadData(id)
    }

}