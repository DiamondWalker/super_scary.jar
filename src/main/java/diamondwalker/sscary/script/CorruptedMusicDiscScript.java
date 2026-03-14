package diamondwalker.sscary.script;

import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.script.variable.*;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.phys.Vec3;

public class CorruptedMusicDiscScript extends Script {
    private final ResourceLocationVariable dimension = ResourceLocationVariable.create().save("dimension").define(this); // TODO: I should just make a level variable
    private final BlockPosVariable jukeboxPos = BlockPosVariable.create().save("jukeboxPos").define(this);
    private final ItemVariable discPlaying = ItemVariable.create().save("disc").define(this);

    private final IntegerVariable ticks = IntegerVariable.create().save("tickCount").define(this);

    public CorruptedMusicDiscScript(MinecraftServer server) {
        super(SScaryScripts.CORRUPTED_MUSIC_DISC.get(), server);
    }

    public CorruptedMusicDiscScript(Level level, BlockPos pos, Item discItem) {
        this(level.getServer());
        dimension.set(level.dimension().location());
        jukeboxPos.set(pos);
        discPlaying.set(discItem);
    }

    @Override
    public void tick() {
        Level level = server.getLevel(ResourceKey.create(Registries.DIMENSION, dimension.get()));

        if (level != null && level.getBlockEntity(jukeboxPos.get()) instanceof JukeboxBlockEntity jukebox) {
            if (jukebox.getTheItem().getItem() == discPlaying.get()) {
                if (discPlaying.get() == Items.MUSIC_DISC_13) {
                    if (ticks.get() == 600) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "..."));
                    } else if (ticks.get() == 680) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "this actually kinda slaps ngl"));
                        end();
                    }
                } else if (discPlaying.get() == Items.MUSIC_DISC_11) {
                    if (ticks.get() == 555) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "BANGER"));
                        end();
                    }
                } else if (discPlaying.get() == Items.MUSIC_DISC_5) {
                    if (ticks.get() == 300) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "holy peak"));
                        end();
                    }
                } else {
                    if (ticks.get() == 180) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "That music is ass"));
                    } else if (ticks.get() == 200) {
                        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level);
                        if (lightningbolt != null) {
                            lightningbolt.moveTo(Vec3.atBottomCenterOf(jukeboxPos.get().above()));
                            lightningbolt.setVisualOnly(false);
                            level.addFreshEntity(lightningbolt);
                            jukebox.popOutTheItem();

                            end();
                        }
                    }
                }
                ticks.set(ticks.get() + 1);
                return;
            }
        }
        end();
    }
}
