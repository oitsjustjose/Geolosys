// package com.oitsjustjose.geolosys.items;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Objects;

// import javax.annotation.Nonnull;
// import javax.annotation.Nullable;

// import com.mojang.blaze3d.platform.GlStateManager;
// import com.oitsjustjose.geolosys.utils.Utils;

// import org.lwjgl.opengl.GL11;

// import net.minecraft.block.BlockState;
// import net.minecraft.client.Minecraft;
// import net.minecraft.client.util.ITooltipFlag;
// import net.minecraft.item.Item;
// import net.minecraft.item.ItemGroup;
// import net.minecraft.item.ItemStack;
// import net.minecraft.util.ActionResult;
// import net.minecraft.util.ResourceLocation;
// import net.minecraft.util.SoundCategory;
// import net.minecraft.util.SoundEvent;
// import net.minecraft.util.math.BlockPos;
// import net.minecraft.util.math.ChunkPos;
// import net.minecraft.world.World;
// import net.minecraftforge.client.event.RenderGameOverlayEvent;
// import net.minecraftforge.common.MinecraftForge;
// import net.minecraftforge.eventbus.api.SubscribeEvent;
// import net.minecraftforge.fml.config.ModConfig;

// public class ItemProPick extends Item
// {
//     private HashMap<Integer, Integer> dimensionSeaLevels;

//     public ItemProPick()
//     {
//         super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).setNoRepair());
//         this.setRegistryName("geolosys", "pro_pick");
//         MinecraftForge.EVENT_BUS.register(this);
//     }

//     /**
//      * @param dimensionSeaLevels the dimensionSeaLevels to set
//      */
//     public void setDimensionSeaLevels(HashMap<Integer, Integer> dimensionSeaLevels)
//     {
//         this.dimensionSeaLevels = dimensionSeaLevels;
//     }

//     @Override
//     public String getUnlocalizedName(@Nonnull ItemStack stack)
//     {
//         return Objects.requireNonNull(stack.getItem().getRegistryName()).toString().replaceAll(":", ".");
//     }

//     @Override
//     public double getDurabilityForDisplay(ItemStack stack)
//     {
//         if (ModConfig.prospecting.enableProPickDamage)
//         {
//             if (stack.getTagCompound() == null)
//             {
//                 stack.setTagCompound(new NBTTagCompound());
//                 stack.getTagCompound().setInteger("damage", ModConfig.prospecting.proPickDurability);
//             }
//             return 1D - (double) stack.getTagCompound().getInteger("damage")
//                     / (double) ModConfig.prospecting.proPickDurability;
//         }
//         else
//         {
//             return 1;
//         }
//     }

//     @SideOnly(Side.CLIENT)
//     public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//     {
//         if (ModConfig.prospecting.enableProPickDamage && Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
//         {
//             if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("damage"))
//             {
//                 tooltip.add("Durability: " + ModConfig.prospecting.proPickDurability);
//             }
//             else
//             {
//                 tooltip.add("Durability: " + stack.getTagCompound().getInteger("damage") + "/"
//                         + ModConfig.prospecting.proPickDurability);
//             }
//         }
//     }

//     @Override
//     public boolean showDurabilityBar(ItemStack stack)
//     {
//         return (ModConfig.prospecting.enableProPickDamage && stack.hasTagCompound());
//     }

//     public void attemptDamageItem(EntityPlayer player, BlockPos pos, EnumHand hand, World worldIn)
//     {
//         if (ModConfig.prospecting.enableProPickDamage && !player.capabilities.isCreativeMode)
//         {
//             if (player.getHeldItem(hand).getItem() instanceof ItemProPick)
//             {
//                 if (player.getHeldItem(hand).getTagCompound() == null)
//                 {
//                     player.getHeldItem(hand).setTagCompound(new NBTTagCompound());
//                     player.getHeldItem(hand).getTagCompound().setInteger("damage",
//                             ModConfig.prospecting.proPickDurability);
//                 }
//                 int prevDmg = player.getHeldItem(hand).getTagCompound().getInteger("damage");
//                 player.getHeldItem(hand).getTagCompound().setInteger("damage", (prevDmg - 1));
//                 if (player.getHeldItem(hand).getTagCompound().getInteger("damage") <= 0)
//                 {
//                     player.setHeldItem(hand, ItemStack.EMPTY);
//                     worldIn.playSound(player, pos, new SoundEvent(new ResourceLocation("entity.item.break")),
//                             SoundCategory.PLAYERS, 1.0F, 0.85F);
//                 }
//             }
//         }
//     }

//     @Override
//     public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
//     {
//         if (player.isSneaking())
//         {
//             ItemStack stack = player.getHeldItem(hand);
//             // If there's no stack compound make one and assume last state was ores
//             if (stack.getTagCompound() == null)
//             {
//                 stack.setTagCompound(new NBTTagCompound());
//                 stack.getTagCompound().setBoolean("stone", true);
//             }
//             // Swap boolean for compound state
//             else
//             {
//                 stack.getTagCompound().setBoolean("stone", !stack.getTagCompound().getBoolean("stone"));
//             }

//             boolean searchForStone = stack.getTagCompound().getBoolean("stone");

//             if (searchForStone)
//             {
//                 player.sendStatusMessage(new TextComponentTranslation("geolosys.pro_pick.tooltip.mode.stones"), true);
//             }
//             else
//             {
//                 player.sendStatusMessage(new TextComponentTranslation("geolosys.pro_pick.tooltip.mode.ores"), true);
//             }
//         }
//         return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
//     }

//     @Override
//     public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
//             EnumFacing facing, float hitX, float hitY, float hitZ)
//     {
//         if (player.isSneaking())
//         {
//             this.onItemRightClick(worldIn, player, hand);
//         }
//         else
//         {
//             this.attemptDamageItem(player, pos, hand, worldIn);
//             // At surface or higher
//             if (worldIn.isRemote)
//             {
//                 player.swingArm(hand);
//                 return EnumActionResult.PASS;
//             }

//             ItemStack stack = player.getHeldItem(hand);

//             // If there's no stack compound make one and assume last state was ores
//             if (stack.getTagCompound() == null)
//             {
//                 stack.setTagCompound(new NBTTagCompound());
//                 stack.getTagCompound().setBoolean("stone", false);
//             }

//             boolean searchForStone = stack.getTagCompound().getBoolean("stone");

//             int seaLvl;
//             try
//             {
//                 seaLvl = this.dimensionSeaLevels.get(player.dimension);
//             }
//             catch (NullPointerException e)
//             {
//                 seaLvl = worldIn.getSeaLevel();
//             }

//             if (player.getPosition().getY() >= seaLvl)
//             {
//                 if (searchForStone)
//                 {
//                     String oreFound = findStoneInChunk(worldIn, pos);
//                     if (oreFound != null)
//                     {
//                         player.sendStatusMessage(
//                                 new TextComponentTranslation("geolosys.pro_pick.tooltip.found_surface", oreFound),
//                                 true);
//                     }
//                     else
//                     {
//                         player.sendStatusMessage(
//                                 new TextComponentTranslation("geolosys.pro_pick.tooltip.nonefound_stone_surface"),
//                                 true);
//                     }
//                 }
//                 else
//                 {
//                     String oreFound = findOreInChunk(worldIn, pos);
//                     if (oreFound != null)
//                     {
//                         player.sendStatusMessage(
//                                 new TextComponentTranslation("geolosys.pro_pick.tooltip.found_surface", oreFound),
//                                 true);
//                     }
//                     else
//                     {
//                         player.sendStatusMessage(
//                                 new TextComponentTranslation("geolosys.pro_pick.tooltip.nonefound_surface"), true);
//                     }
//                 }
//             }
//             else
//             {
//                 int xStart;
//                 int xEnd;
//                 int yStart;
//                 int yEnd;
//                 int zStart;
//                 int zEnd;
//                 int confAmt = ModConfig.prospecting.proPickRange;
//                 int confDmt = ModConfig.prospecting.proPickDiameter;

//                 boolean found = false;

//                 switch (facing)
//                 {
//                 case UP:
//                     xStart = -(confDmt / 2);
//                     xEnd = confDmt / 2;
//                     yStart = -confAmt;
//                     yEnd = 0;
//                     zStart = -(confDmt / 2);
//                     zEnd = (confDmt / 2);
//                     found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
//                     break;
//                 case DOWN:
//                     xStart = -(confDmt / 2);
//                     xEnd = confDmt / 2;
//                     yStart = 0;
//                     yEnd = confAmt;
//                     zStart = -(confDmt / 2);
//                     zEnd = confDmt / 2;
//                     found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
//                     break;
//                 case NORTH:
//                     xStart = -(confDmt / 2);
//                     xEnd = confDmt / 2;
//                     yStart = -(confDmt / 2);
//                     yEnd = confDmt / 2;
//                     zStart = 0;
//                     zEnd = confAmt;
//                     found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
//                     break;
//                 case SOUTH:
//                     xStart = -(confDmt / 2);
//                     xEnd = confDmt / 2;
//                     yStart = -(confDmt / 2);
//                     yEnd = confDmt / 2;
//                     zStart = -confAmt;
//                     zEnd = 0;
//                     found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
//                     break;
//                 case EAST:
//                     xStart = -(confAmt);
//                     xEnd = 0;
//                     yStart = -(confDmt / 2);
//                     yEnd = confDmt / 2;
//                     zStart = -(confDmt / 2);
//                     zEnd = confDmt / 2;
//                     found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
//                     break;
//                 case WEST:
//                     xStart = 0;
//                     xEnd = confAmt;
//                     yStart = -(confDmt / 2);
//                     yEnd = confDmt / 2;
//                     zStart = -(confDmt / 2);
//                     zEnd = confDmt / 2;
//                     found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
//                     break;
//                 }
//                 // If right clicking yielded nothing, then find the ore in the chunk again
//                 if (!found)
//                 {
//                     String oreFound = findOreInChunk(worldIn, pos);
//                     if (oreFound != null)
//                     {
//                         player.sendStatusMessage(
//                                 new TextComponentTranslation("geolosys.pro_pick.tooltip.found_surface", oreFound),
//                                 true);
//                     }
//                     else
//                     {
//                         player.sendStatusMessage(
//                                 new TextComponentTranslation("geolosys.pro_pick.tooltip.nonefound_surface"), true);
//                     }
//                 }
//             }
//             player.swingArm(hand);
//         }
//         return EnumActionResult.SUCCESS;
//     }

//     private boolean isFound(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, int xStart, int xEnd,
//             int yStart, int yEnd, int zStart, int zEnd)
//     {
//         boolean found = false;
//         for (int x = xStart; x <= xEnd; x++)
//         {
//             for (int y = yStart; y <= yEnd; y++)
//             {
//                 for (int z = zStart; z <= zEnd; z++)
//                 {
//                     BlockState state = worldIn.getBlockState(pos.add(x, y, z));
//                     boolean shouldSkip = false;
//                     for (IOre stone : GeolosysAPI.stones)
//                     {
//                         if (Utils.doStatesMatch(state, stone.getOre()))
//                         {
//                             shouldSkip = true;
//                         }
//                     }
//                     if (shouldSkip)
//                     {
//                         continue;
//                     }
//                     // if (GeolosysAPI.oreBlocks.keySet().contains(state))
//                     for (IOre ore : GeolosysAPI.oreBlocks)
//                     {
//                         if (ore instanceof DepositMultiOre)
//                         {
//                             DepositMultiOre tmp = (DepositMultiOre) ore;
//                             for (BlockState tmpState : tmp.oreBlocks.keySet())
//                             {
//                                 if (Utils.doStatesMatch(tmpState, state))
//                                 {
//                                     sendFoundMessage(player, state, facing);
//                                     found = true;
//                                     break;
//                                 }
//                             }
//                         }
//                         else
//                         {
//                             if (Utils.doStatesMatch(ore.getOre(), state))
//                             {
//                                 sendFoundMessage(player, state, facing);
//                                 found = true;
//                                 break;
//                             }
//                         }
//                     }
//                     if (!found)
//                     {
//                         for (BlockState state2 : GeolosysAPI.proPickExtras)
//                         {
//                             if (Utils.doStatesMatch(state2, state))
//                             {
//                                 sendFoundMessage(player, state, facing);
//                                 found = true;
//                                 break;
//                             }
//                         }
//                     }
//                     if (found)
//                     {
//                         break;
//                     }
//                 }
//                 if (found)
//                 {
//                     break;
//                 }
//             }
//             if (found)
//             {
//                 break;
//             }
//         }
//         return found;
//     }

//     private void sendFoundMessage(EntityPlayer player, BlockState state, EnumFacing facing)
//     {
//         player.sendStatusMessage(new TextComponentTranslation("geolosys.pro_pick.tooltip.found",
//                 new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName(),
//                 facing.getOpposite()), true);
//     }

//     @SubscribeEvent
//     @SideOnly(Side.CLIENT)
//     public void onDrawScreen(RenderGameOverlayEvent.Post event)
//     {
//         if (event.getType() != RenderGameOverlayEvent.ElementType.ALL
//                 || Minecraft.getMinecraft().debugRenderer.shouldRender()
//                 || Minecraft.getMinecraft().gameSettings.showDebugInfo
//                 || Minecraft.getMinecraft().gameSettings.showDebugProfilerChart)
//         {
//             return;
//         }
//         Minecraft mc = Minecraft.getMinecraft();
//         if (mc.player.getHeldItemMainhand().getItem() instanceof ItemProPick
//                 || mc.player.getHeldItemOffhand().getItem() instanceof ItemProPick)
//         {
//             GlStateManager.enableBlend();
//             GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//             GlStateManager.disableLighting();
//             int seaLvl;
//             try
//             {
//                 seaLvl = this.dimensionSeaLevels.get(mc.player.dimension);
//             }
//             catch (NullPointerException e)
//             {
//                 seaLvl = mc.player.getEntityWorld().getSeaLevel();
//             }
//             int level = (int) (seaLvl - mc.player.posY);
//             if (level < 0)
//             {
//                 mc.fontRenderer.drawStringWithShadow("Depth: " + Math.abs(level) + "m above sea-level",
//                         ModConfig.client.hudX, ModConfig.client.hudY, 0xFFFFFFFF);
//             }
//             else if (level == 0)
//             {
//                 mc.fontRenderer.drawStringWithShadow("Depth: at sea-level", ModConfig.client.hudX,
//                         ModConfig.client.hudY, 0xFFFFFFFF);
//             }
//             else
//             {
//                 mc.fontRenderer.drawStringWithShadow("Depth: " + level + "m below sea-level", ModConfig.client.hudX,
//                         ModConfig.client.hudY, 0xFFFFFFFF);
//             }
//             GlStateManager.color(1F, 1F, 1F, 1F);
//         }
//     }

//     private String findOreInChunk(World world, BlockPos pos)
//     {
//         ChunkPos tempPos = new ChunkPos(pos);

//         SURFACE_PROSPECTING_TYPE searchType = ModConfig.prospecting.surfaceProspectingResults;

//         for (int x = tempPos.getXStart(); x <= tempPos.getXEnd(); x++)
//         {
//             for (int z = tempPos.getZStart(); z <= tempPos.getZEnd(); z++)
//             {
//                 for (int y = 0; y < world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(); y++)
//                 {
//                     BlockState state = world.getBlockState(new BlockPos(x, y, z));

//                     // We want to specifically ignore any stone-like blocks; say granite generates,
//                     // it may throw a false-positive result for Autunite & Granite
//                     boolean shouldSkip = false;
//                     for (DepositStone stone : GeolosysAPI.stones)
//                     {
//                         if (Utils.doStatesMatch(stone.getOre(), state)
//                                 && searchType == SURFACE_PROSPECTING_TYPE.OREBLOCKS)
//                         {
//                             shouldSkip = true;
//                         }
//                     }
//                     if (shouldSkip)
//                     {
//                         continue;
//                     }
//                     for (IOre ore : GeolosysAPI.oreBlocks)
//                     {
//                         if (ore instanceof DepositMultiOre)
//                         {
//                             DepositMultiOre multiOre = (DepositMultiOre) ore;
//                             for (BlockState multiOreState : (searchType == SURFACE_PROSPECTING_TYPE.OREBLOCKS
//                                     ? multiOre.oreBlocks.keySet()
//                                     : multiOre.sampleBlocks.keySet()))
//                             {
//                                 if (Utils.doStatesMatch(state, multiOreState))
//                                 {
//                                     return multiOre.getFriendlyName();
//                                 }
//                             }
//                         }
//                         else
//                         {
//                             if (Utils.doStatesMatch(state,
//                                     (searchType == SURFACE_PROSPECTING_TYPE.OREBLOCKS ? ore.getOre()
//                                             : ore.getSample())))
//                             {
//                                 return ore.getFriendlyName();
//                             }
//                         }
//                     }
//                     for (BlockState state2 : GeolosysAPI.proPickExtras)
//                     {
//                         if (Utils.doStatesMatch(state, state2))
//                         {
//                             return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state))
//                                     .getDisplayName();
//                         }
//                     }
//                 }
//             }
//         }

//         return null;
//     }

//     private String findStoneInChunk(World world, BlockPos pos)
//     {
//         ChunkPos tempPos = new ChunkPos(pos);

//         for (int x = tempPos.getXStart(); x <= tempPos.getXEnd(); x++)
//         {
//             for (int z = tempPos.getZStart(); z <= tempPos.getZEnd(); z++)
//             {
//                 for (int y = 0; y < world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(); y++)
//                 {
//                     for (DepositStone s : GeolosysAPI.stones)
//                     {
//                         if (Utils.doStatesMatch(s.getOre(), world.getBlockState(new BlockPos(x, y, z))))
//                         {
//                             return s.getFriendlyName();
//                         }
//                     }
//                 }
//             }
//         }

//         return null;

//     }
// }