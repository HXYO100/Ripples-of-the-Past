package com.github.standobyte.jojo.action.actions;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.client.sound.ClientTickingSoundsHelper;
import com.github.standobyte.jojo.entity.mob.HungryZombieEntity;
import com.github.standobyte.jojo.init.ModEffects;
import com.github.standobyte.jojo.init.ModNonStandPowers;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.nonstand.type.VampirismPowerType;
import com.github.standobyte.jojo.util.JojoModUtil;
import com.github.standobyte.jojo.util.damage.ModDamageSources;

import net.minecraft.entity.Entity;
import net.minecraft.entity.INPC;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VampirismBloodDrain extends Action {

    public VampirismBloodDrain(AbstractBuilder<?> builder) {
        super(builder);
    }
    
    @Override
    public ActionConditionResult checkConditions(LivingEntity user, LivingEntity performer, IPower<?> power, ActionTarget target) {
        if (user.level.getDifficulty() == Difficulty.PEACEFUL) {
            return conditionMessage("peaceful");
        }
        if (!performer.getMainHandItem().isEmpty()) {
            return conditionMessage("hand");
        }
        Entity entityTarget = target.getEntity(performer.level);
        if (entityTarget instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity) entityTarget;
            if (!JojoModUtil.canBleed(livingTarget) || JojoModUtil.isUndead(livingTarget)) {
                return livingTarget.tickCount > 20 ? conditionMessage("blood") : ActionConditionResult.NEGATIVE;
            }
        }
        else {
            return ActionConditionResult.NEGATIVE;
        }
        return ActionConditionResult.POSITIVE;
    }
    
    @Override
    public void onHoldTickUser(World world, LivingEntity user, IPower<?> power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (requirementsFulfilled) {
            if (!world.isClientSide() && target.getEntity(world) instanceof LivingEntity) {
                LivingEntity targetEntity = (LivingEntity) target.getEntity(world);
                if (!targetEntity.isDeadOrDying()) {
                    int difficulty = world.getDifficulty().getId();
                    float manaAndHealModifier = 0.25F + difficulty * 0.75F;
                    if (targetEntity instanceof PlayerEntity) {
                        manaAndHealModifier *= 3F;
                    }
                    else if (targetEntity instanceof INPC || targetEntity instanceof AbstractIllagerEntity) {
                        manaAndHealModifier *= 2F;
                    }
                    if (INonStandPower.getNonStandPowerOptional(targetEntity).map(
                            p -> p.getType() == ModNonStandPowers.HAMON.get()).orElse(false)) {
                        manaAndHealModifier *= 6.667F;
                    }
                    float damage = 0.5F * difficulty;
                    EffectInstance freeze = targetEntity.getEffect(ModEffects.FREEZE.get());
                    if (freeze != null) {
                        damage *= 1 - Math.min((freeze.getAmplifier() + 1) * 0.2F, 1);
                    }
                    power.addMana(damage * manaAndHealModifier);
                    damage *= 5;
                    float healed = user.getHealth();
                    if (drainBlood(user, targetEntity, damage, damage * 0.1F * manaAndHealModifier)) {
                        healed = user.getHealth() - healed;
                        if (healed > 0) {
                            power.addMana(healed * VampirismPowerType.healCost(world.getDifficulty()));
                        }
                        if (targetEntity.isDeadOrDying()) {
                            HungryZombieEntity.createZombie((ServerWorld) world, user, targetEntity, false);
                        }
                    }
                }
            }
        }
    }

    private static final Effect[] BLOOD_DRAIN_EFFECTS = {
            Effects.MOVEMENT_SLOWDOWN,
            Effects.DIG_SLOWDOWN,
            Effects.WEAKNESS,
            Effects.CONFUSION
    };
    public static boolean drainBlood(LivingEntity attacker, LivingEntity target, float bloodDrainDamage, float healAmount) {
        boolean hurt = target.hurt(ModDamageSources.bloodDrainDamage(attacker), bloodDrainDamage);
        if (hurt) {
            attacker.heal(healAmount);
            int effectsLvl = attacker.level.getDifficulty().getId() - 1;
            if (effectsLvl >= 0) {
                for (Effect effect : BLOOD_DRAIN_EFFECTS) {
                    int duration = MathHelper.floor(20F * bloodDrainDamage);
                    EffectInstance effectInstance = target.getEffect(effect);
                    EffectInstance newInstance = effectInstance == null ? 
                            new EffectInstance(effect, duration, effectsLvl)
                            : new EffectInstance(effect, effectInstance.getDuration() + duration, effectsLvl);
                    target.addEffect(newInstance);
                }
            }
        }
        return hurt;
    }
    
    @Override
    public boolean isHeldSentToTracking() {
        return true;
    }
    
    @Override
    public void onHoldTickClientEffect(LivingEntity user, IPower<?> power, int ticksHeld, boolean requirementsFulfilled, boolean stateRefreshed) {
        if (stateRefreshed && requirementsFulfilled) {
            ClientTickingSoundsHelper.playHeldActionSound(ModSounds.VAMPIRE_BLOOD_DRAIN.get(), 1.0F, 1.0F, true, getPerformer(user, power), power, this);
        }
    }
}
