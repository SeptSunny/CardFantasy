package cfvbaibai.cardfantasy.engine.feature;

import java.util.ArrayList;
import java.util.List;

import cfvbaibai.cardfantasy.GameUI;
import cfvbaibai.cardfantasy.data.Skill;
import cfvbaibai.cardfantasy.engine.CardInfo;
import cfvbaibai.cardfantasy.engine.CardStatusItem;
import cfvbaibai.cardfantasy.engine.CardStatusType;
import cfvbaibai.cardfantasy.engine.EntityInfo;
import cfvbaibai.cardfantasy.engine.FeatureInfo;
import cfvbaibai.cardfantasy.engine.FeatureResolver;
import cfvbaibai.cardfantasy.engine.HeroDieSignal;
import cfvbaibai.cardfantasy.engine.OnAttackBlockingResult;
import cfvbaibai.cardfantasy.engine.Player;

public final class BurningFlameFeature {
    public static void apply(FeatureInfo featureInfo, FeatureResolver resolver, EntityInfo attacker, Player defender)
            throws HeroDieSignal {
        Skill skill = featureInfo.getFeature();
        int damage = skill.getImpact();
        List<CardInfo> candidates = resolver.getStage().getRandomizer().pickRandom(
            defender.getField().toList(), -1, true, null);
        CardStatusItem newBurningStatus = CardStatusItem.burning(damage, featureInfo);
        List<CardInfo> victims = new ArrayList<CardInfo>();
        for (CardInfo candidate : candidates) {
            /*
             * Burning status of the same level could not be stacked.
             * This seems to be an bug in official version
             */
            boolean skipped = false;
            for (CardStatusItem existingBurningStatus : candidate.getStatus().getStatusOf(CardStatusType.燃烧)) {
                if (existingBurningStatus.getEffect() == newBurningStatus.getEffect()) {
                    skipped = true;
                    break;
                }
            }
            if (!skipped) {
                victims.add(candidate);
            }
        }
        GameUI ui = resolver.getStage().getUI();
        ui.useSkill(attacker, victims, skill, true);
        for (CardInfo victim : victims) {
            OnAttackBlockingResult result = resolver.resolveAttackBlockingFeature(attacker, victim, skill, damage);
            if (!result.isAttackable()) {
                continue;
            }
            ui.addCardStatus(attacker, victim, skill, newBurningStatus);
            victim.addStatus(newBurningStatus);
        }
    }
}
