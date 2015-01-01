package cfvbaibai.cardfantasy.engine.feature;

import cfvbaibai.cardfantasy.GameUI;
import cfvbaibai.cardfantasy.data.Skill;
import cfvbaibai.cardfantasy.data.SkillTag;
import cfvbaibai.cardfantasy.engine.CardInfo;
import cfvbaibai.cardfantasy.engine.EntityInfo;
import cfvbaibai.cardfantasy.engine.FeatureResolver;

public final class ImmobilityFeature {
    public static boolean isFeatureBlocked(FeatureResolver resolver, Skill cardFeature, Skill attackFeature,
            EntityInfo attacker, CardInfo defender) {
        if (attackFeature.getType().containsTag(SkillTag.即死)) {
            GameUI ui = resolver.getStage().getUI();
            ui.useSkill(defender, attacker, cardFeature, true);
            ui.blockFeature(attacker, defender, cardFeature, attackFeature);
            return true;
        } else {
            return false;
        }
    }
}
