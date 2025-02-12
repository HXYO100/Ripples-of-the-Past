package com.github.standobyte.jojo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.capability.entity.ClientPlayerUtilCapProvider;
import com.github.standobyte.jojo.capability.entity.LivingUtilCapProvider;
import com.github.standobyte.jojo.capability.entity.PlayerUtilCapProvider;
import com.github.standobyte.jojo.capability.entity.ProjectileHamonChargeCapProvider;
import com.github.standobyte.jojo.capability.entity.power.NonStandCapProvider;
import com.github.standobyte.jojo.capability.entity.power.StandCapProvider;
import com.github.standobyte.jojo.capability.world.SaveFileUtilCapProvider;
import com.github.standobyte.jojo.capability.world.WorldUtilCapProvider;
import com.github.standobyte.jojo.command.HamonCommand;
import com.github.standobyte.jojo.command.JojoControlsCommand;
import com.github.standobyte.jojo.command.JojoPowerCommand;
import com.github.standobyte.jojo.command.StandCommand;
import com.github.standobyte.jojo.command.StandExpCommand;
import com.github.standobyte.jojo.init.ModStructures;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromserver.UpdateClientCapCachePacket;
import com.github.standobyte.jojo.power.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.stand.IStandPower;
import com.github.standobyte.jojo.util.reflection.CommonReflection;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = JojoMod.MOD_ID)
public class ForgeBusEventSubscriber {
    private static final ResourceLocation STAND_CAP = new ResourceLocation(JojoMod.MOD_ID, "stand");
    private static final ResourceLocation NON_STAND_CAP = new ResourceLocation(JojoMod.MOD_ID, "non_stand");
    private static final ResourceLocation PLAYER_UTIL_CAP = new ResourceLocation(JojoMod.MOD_ID, "player_util");
    private static final ResourceLocation CLIENT_PLAYER_UTIL_CAP = new ResourceLocation(JojoMod.MOD_ID, "client_player_util");
    private static final ResourceLocation LIVING_UTIL_CAP = new ResourceLocation(JojoMod.MOD_ID, "living_util");
    private static final ResourceLocation PROJECTILE_HAMON_CAP = new ResourceLocation(JojoMod.MOD_ID, "projectile_hamon");
    private static final ResourceLocation WORLD_UTIL_CAP = new ResourceLocation(JojoMod.MOD_ID, "world_util");
    private static final ResourceLocation SAVE_FILE_UTIL_CAP = new ResourceLocation(JojoMod.MOD_ID, "save_file_util");
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        StandCommand.register(dispatcher);
        JojoPowerCommand.register(dispatcher);
        StandExpCommand.register(dispatcher);
        JojoControlsCommand.register(dispatcher);
        HamonCommand.register(dispatcher);
    }
    
    @SubscribeEvent
    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        event.addCapability(WORLD_UTIL_CAP, new WorldUtilCapProvider(world));
        if (!world.isClientSide() && world.dimension() == World.OVERWORLD) {
            event.addCapability(SAVE_FILE_UTIL_CAP, new SaveFileUtilCapProvider());
        }
    }
    
    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getObject();
                event.addCapability(STAND_CAP, new StandCapProvider(player));
                event.addCapability(NON_STAND_CAP, new NonStandCapProvider(player));
                event.addCapability(PLAYER_UTIL_CAP, new PlayerUtilCapProvider(player));
                if (player.level.isClientSide()) {
                    event.addCapability(CLIENT_PLAYER_UTIL_CAP, new ClientPlayerUtilCapProvider(player));
                }
            }
            event.addCapability(LIVING_UTIL_CAP, new LivingUtilCapProvider((LivingEntity) entity));
        }
        if (GameplayEventHandler.projectileCanBeChargedWithHamon(entity)) {
            event.addCapability(PROJECTILE_HAMON_CAP, new ProjectileHamonChargeCapProvider(entity));
        }
    }
    
    @SubscribeEvent
    public static void onEntityTracking(PlayerEvent.StartTracking event) {
        Entity entityTracked = event.getTarget();
        if (entityTracked instanceof LivingEntity) {
            LivingEntity livingTracked = (LivingEntity) entityTracked;
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            IStandPower.getStandPowerOptional(livingTracked).ifPresent(power -> {
                power.syncWithTrackingOrUser(player);
            });
            INonStandPower.getNonStandPowerOptional(livingTracked).ifPresent(power -> {
                power.syncWithTrackingOrUser(player);
            });
            livingTracked.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                cap.onTracking(player);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        IStandPower.getPlayerStandPower(player).onClone(IStandPower.getPlayerStandPower(original), 
                event.isWasDeath(), !event.isWasDeath() || JojoModConfig.COMMON.keepStandOnDeath.get());
        INonStandPower.getPlayerNonStandPower(player).onClone(INonStandPower.getPlayerNonStandPower(original), 
                event.isWasDeath(), !event.isWasDeath() || JojoModConfig.COMMON.keepNonStandOnDeath.get());
        
        original.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(oldCap -> {
            player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(newCap -> {
                newCap.moveNotificationsSet(oldCap);
            });
        });
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        syncPowerData(event.getPlayer());
    }
    
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        syncPowerData(event.getPlayer());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        syncPowerData(event.getPlayer());
    }
    
    private static void syncPowerData(PlayerEntity player) {
        IStandPower.getPlayerStandPower(player).syncWithUserOnly();
        INonStandPower.getPlayerNonStandPower(player).syncWithUserOnly();
        PacketManager.sendToClient(new UpdateClientCapCachePacket(), (ServerPlayerEntity) player);
    }
    
    
    
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            
            ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey(CommonReflection.getCodec(serverWorld.getChunkSource().getGenerator()));
            if (cgRL != null && cgRL.getNamespace().equals("terraforged")) {
                return;
            }
            
            if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) {
                return;
            }

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(
                    serverWorld.getChunkSource().getGenerator().getSettings().structureConfig());
            for (RegistryObject<Structure<?>> structure : ModStructures.STRUCTURES.getEntries()) {
                tempMap.putIfAbsent(structure.get(), DimensionStructuresSettings.DEFAULTS.get(structure.get()));
            }
            serverWorld.getChunkSource().getGenerator().getSettings().structureConfig = tempMap;
        }
    }

    public static final Map<Supplier<StructureFeature<?, ?>>, Predicate<BiomeLoadingEvent>> structureBiomes = new HashMap<>();
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        List<Supplier<StructureFeature<?, ?>>> structureStarts = event.getGeneration().getStructures();
        
        for (Map.Entry<Supplier<StructureFeature<?, ?>>, Predicate<BiomeLoadingEvent>> entry : structureBiomes.entrySet()) {
            if (entry.getValue() != null && entry.getValue().test(event)) {
                structureStarts.add(entry.getKey());
            }
        }
    }
}
