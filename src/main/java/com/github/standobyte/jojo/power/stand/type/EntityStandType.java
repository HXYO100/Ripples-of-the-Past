package com.github.standobyte.jojo.power.stand.type;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.standobyte.jojo.action.actions.StandAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromserver.TrSetStandEntityPacket;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.stand.IStandManifestation;
import com.github.standobyte.jojo.power.stand.IStandPower;
import com.github.standobyte.jojo.power.stand.StandPower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;

public class EntityStandType extends StandType {
    private final Supplier<? extends StandEntityType<? extends StandEntity>> entityTypeSupplier;

    public EntityStandType(int tier, int color, ITextComponent partName, StandAction[] attacks, StandAction[] abilities, 
            Supplier<SoundEvent> summonShoutSupplier, Supplier<? extends StandEntityType<? extends StandEntity>> entityTypeSupplier) {
        super(tier, color, partName, attacks, abilities, summonShoutSupplier);
        this.entityTypeSupplier = entityTypeSupplier;
    }

    public StandEntityType<? extends StandEntity> getEntityType() {
        return entityTypeSupplier.get();
    }

    @Override
    public boolean canTickMana(LivingEntity user, IPower<?> power) {
        return !power.isActive() || user.tickCount % 20 == 0 && user instanceof PlayerEntity && ((PlayerEntity) user).getFoodData().getFoodLevel() > 17;
    }

    @Override
    public boolean summon(LivingEntity user, IStandPower standPower, boolean withoutNameVoiceLine) {
        return summon(user, standPower, entity -> {}, withoutNameVoiceLine);
    }

    public boolean summon(LivingEntity user, IStandPower standPower, Consumer<StandEntity> beforeTheSummon, boolean withoutNameVoiceLine) {
        if (!super.summon(user, standPower, withoutNameVoiceLine)) {
            return false;
        }
        if (!user.level.isClientSide()) {
            StandEntity standEntity = getEntityType().create(user.level);
            standEntity.copyPosition(user);
            standPower.setStandManifestation(standEntity);
            beforeTheSummon.accept(standEntity);
            user.level.addFreshEntity(standEntity);
            
            List<Effect> effectsToCopy = standEntity.getEffectsSharedToStand();
            for (Effect effect : effectsToCopy) {
                EffectInstance userEffectInstance = user.getEffect(effect);
                if (userEffectInstance != null) {
                    standEntity.addEffect(new EffectInstance(userEffectInstance));
                }
            }
            PacketManager.sendToClientsTrackingAndSelf(new TrSetStandEntityPacket(user.getId(), standEntity.getId()), user);
        }
        return true;
    }

    @Override
    public void unsummon(LivingEntity user, IStandPower standPower) {
        if (!user.level.isClientSide()) {
            StandEntity standEntity = ((StandEntity) standPower.getStandManifestation());
            if (standEntity != null) {
                standEntity.retractStand(true);
            }
        }
    }

    @Override
    public void tickUser(LivingEntity user, IPower<?> power) {
        super.tickUser(user, power);
        IStandManifestation stand = ((StandPower) power).getStandManifestation();
        if (stand instanceof StandEntity) {
            StandEntity standEntity = (StandEntity) stand;
            if (standEntity.level != user.level) {
                forceUnsummon(user, (IStandPower) power);
            }
        }
    }

    @Override
    public void forceUnsummon(LivingEntity user, IStandPower standPower) {
        if (!user.level.isClientSide()) {
            IStandManifestation stand = standPower.getStandManifestation();
            if (stand instanceof StandEntity) {
                StandEntity standEntity = (StandEntity) stand;
                standPower.setStandManifestation(null);
                PacketManager.sendToClientsTrackingAndSelf(new TrSetStandEntityPacket(user.getId(), -1), user);
                standEntity.remove();
            }
        }
    }
    

    
    public static void giveSharedEffectsFromUser(PotionAddedEvent event) {
        IStandPower.getStandPowerOptional(event.getEntityLiving()).ifPresent(power -> {
            if (power.isActive() && power.getStandManifestation() instanceof StandEntity) {
                EffectInstance effectInstance = event.getPotionEffect();
                StandEntity stand = (StandEntity) power.getStandManifestation();
                if (stand.getEffectsSharedToStand().contains(effectInstance.getEffect())) {
                    stand.addEffect(new EffectInstance(effectInstance));
                }
            }
        });
    }
}
