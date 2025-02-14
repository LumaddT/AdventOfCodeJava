package advent.of.code.solutions.y2018.day24;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
class Group {
    @Getter
    private int Units;
    private final int HitPoints;
    private int AttackDamage;
    private final String AttackType;
    private final int Initiative;
    private final Set<String> Immunities;
    private final Set<String> Weaknesses;
    @Getter
    private final Sides Side;

    public int getEffectivePower() {
        return Units * AttackDamage;
    }

    // The natural ordering of groups when selecting targets is in decreasing order of
    // EffectivePower, with Initiative as the tiebreaker
    public int compareToSelection(Group other) {
        if (other.getEffectivePower() != this.getEffectivePower()) {
            return other.getEffectivePower() - this.getEffectivePower();
        }

        return other.Initiative - this.Initiative;
    }

    // The natural ordering of groups when attacking is in decreasing order of Initiative
    public int compareToAttack(Group other) {
        return other.Initiative - this.Initiative;
    }

    public int damageTo(Group defender) {
        if (this.Side == defender.Side) {
            return 0;
        }

        boolean isDefenderImmune = defender.Immunities.contains(this.AttackType);
        boolean isDefenderWeak = defender.Weaknesses.contains(this.AttackType);

        if (isDefenderImmune && isDefenderWeak) {
            throw new RuntimeException("Impossible immunity/weakness state");
        }

        if (isDefenderImmune) {
            return 0;
        }

        int returnValue = this.getEffectivePower();

        if (isDefenderWeak) {
            returnValue *= 2;
        }

        return returnValue;
    }

    public boolean isBetterTargetThan(Group other) {
        if (this.getEffectivePower() > other.getEffectivePower()) {
            return true;
        }

        if (this.getEffectivePower() < other.getEffectivePower()) {
            return false;
        }

        return this.Initiative > other.Initiative;
    }

    public boolean attack(Group defender) {
        int totalDamage = this.damageTo(defender);
        int unitsKilled = totalDamage / defender.HitPoints;
        defender.Units -= unitsKilled;

        return unitsKilled != 0;
    }

    public boolean isAlive() {
        return Units > 0;
    }

    public void applyBoost(int boost) {
        AttackDamage += boost;
    }
}
