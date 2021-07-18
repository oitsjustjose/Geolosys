// package com.oitsjustjose.geolosys.common.data.serializer;

// import java.lang.reflect.Type;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;

// import com.google.gson.JsonDeserializationContext;
// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;
// import com.google.gson.JsonParser;
// import com.google.gson.JsonSerializationContext;
// import com.oitsjustjose.geolosys.Geolosys;
// import com.oitsjustjose.geolosys.api.PlutonType;
// import com.oitsjustjose.geolosys.api.world.Deposit;
// import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
// import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
// import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
// import com.oitsjustjose.geolosys.api.world.IDeposit;
// import com.oitsjustjose.geolosys.common.utils.Utils;

// import net.minecraft.block.BlockState;
// import net.minecraft.world.biome.Biome;
// import net.minecraftforge.common.BiomeDictionary;

// public class OreConfigSerializer {
//     public IDeposit deserialize(JsonObject jsonConfig, Type type, JsonDeserializationContext ctx) {

//         if (jsonConfig == null) {
//             return null;
//         }

//         try {
//             if (jsonConfig.has("blocks") && jsonConfig.has("samples")) {
//                 if (jsonConfig.has("biomes")) {
//                     return deserializeDepositMultiOreBiomeRestricted(jsonConfig);
//                 }
//                 return deserializeDepositMultiOre(jsonConfig);
//             } else if (jsonConfig.has("biomes")) {
//                 return deserializeDepositBiomeRestricted(jsonConfig);
//             } else if (jsonConfig.has("block") && jsonConfig.has("sample")) {
//                 return deserializeDeposit(jsonConfig);
//             }

//             Geolosys.getInstance().LOGGER.error("Given JSON file has a mix of 'block(s)' and 'sample(s)'");
//             return null;
//         } catch (Exception e) {
//             Geolosys.getInstance().LOGGER.error("Failed to parse JSON file: {}", e);
//             return null;
//         }
//     }

//     public JsonElement serialize(IDeposit dep, Type type, JsonSerializationContext ctx) {
//         JsonObject json = new JsonObject();
//         JsonObject config = new JsonObject();
//         JsonParser parser = new JsonParser();
//         JsonObject dim = new JsonObject();

//         // Add custom logic for the dimension blacklist
//         dim.addProperty("isBlacklist", true);
//         dim.add("filter", parser.parse(Arrays.toString(new String[] {})));

//         // Add the base set
//         config.addProperty("size", dep.getSize());
//         config.addProperty("chance", dep.getChance());
//         config.addProperty("yMin", dep.getYMin());
//         config.addProperty("yMax", dep.getYMax());
//         config.addProperty("type", dep.getPlutonType().name());
//         config.addProperty("density", dep.getDensity());
//         config.add("dimensions", dim);

//         // Now add extras
//         if (dep instanceof DepositMultiOre) {
//             DepositMultiOre dmo = (DepositMultiOre) dep;
//             config.add("blocks", Utility.deconstructMultiBlockMap(dmo.oreBlocks));
//             config.add("samples", Utility.deconstructMultiBlockMap(dmo.sampleBlocks));
//         } else {
//             config.addProperty("block", dep.getOre().getBlock().getRegistryName().toString());
//             config.addProperty("sample", dep.getSampleBlock().getBlock().getRegistryName().toString());
//         }

//         if (dep instanceof DepositBiomeRestricted || dep instanceof DepositMultiOreBiomeRestricted) {
//             DepositBiomeRestricted dbr = (DepositBiomeRestricted) dep;
//             config.add("biomes", Utility.deconstructBiomes(dbr.getBiomeList(), dbr.getBiomeTypes()));
//             config.addProperty("isWhitelist", dbr.useWhitelist());
//         }

//         json.addProperty("type", "geolosys:ore_deposit");
//         json.add("config", config);

//         return json;
//     }

//     private Deposit deserializeDeposit(JsonObject json) {
//         try {
//             BlockState block = Utility.fromString(json.get("block").getAsString());
//             BlockState sample = Utility.fromString(json.get("sample").getAsString());
//             int size = json.get("size").getAsInt();
//             int chance = json.get("chance").getAsInt();
//             int yMin = json.get("yMin").getAsInt();
//             int yMax = json.get("yMax").getAsInt();
//             String[] dimFilter = Utility.getDimFilter(json);
//             boolean isDimFilterBl = Utility.getIsDimFilterBl(json);
//             HashSet<BlockState> blockStateMatchers = Utils.getDefaultMatchers();
//             PlutonType type = PlutonType.valueOf(json.get("type").getAsString());
//             float density = json.get("density").getAsFloat();
//             if (json.has("blockStateMatchers")) {
//                 blockStateMatchers = Utility.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
//             }

//             return new Deposit(block, sample, yMin, yMax, size, chance, dimFilter, isDimFilterBl, blockStateMatchers,
//                     type, density);
//         } catch (Exception e) {
//             Geolosys.getInstance().LOGGER.error("Failed to parse JSON file: {}", e);
//             return null;
//         }
//     }

//     private DepositMultiOre deserializeDepositMultiOre(JsonObject json) {
//         try {
//             HashMap<BlockState, Integer> blocks = Utility.buildMultiBlockMap(json.get("blocks").getAsJsonArray());
//             HashMap<BlockState, Integer> samples = Utility.buildMultiBlockMap(json.get("samples").getAsJsonArray());
//             int size = json.get("size").getAsInt();
//             int chance = json.get("chance").getAsInt();
//             int yMin = json.get("yMin").getAsInt();
//             int yMax = json.get("yMax").getAsInt();
//             String[] dimFilter = Utility.getDimFilter(json);
//             boolean isDimFilterBl = Utility.getIsDimFilterBl(json);
//             HashSet<BlockState> blockStateMatchers = Utils.getDefaultMatchers();
//             PlutonType type = PlutonType.valueOf(json.get("type").getAsString());
//             float density = json.get("density").getAsFloat();
//             if (json.has("blockStateMatchers")) {
//                 blockStateMatchers = Utility.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
//             }

//             return new DepositMultiOre(blocks, samples, yMin, yMax, size, chance, dimFilter, isDimFilterBl,
//                     blockStateMatchers, type, density);
//         } catch (Exception e) {
//             Geolosys.getInstance().LOGGER.error("Failed to parse JSON file: {}", e);
//             return null;
//         }
//     }

//     private DepositBiomeRestricted deserializeDepositBiomeRestricted(JsonObject json) {
//         try {
//             BlockState block = Utility.fromString(json.get("block").getAsString());
//             BlockState sample = Utility.fromString(json.get("sample").getAsString());
//             int size = json.get("size").getAsInt();
//             int chance = json.get("chance").getAsInt();
//             int yMin = json.get("yMin").getAsInt();
//             int yMax = json.get("yMax").getAsInt();
//             String[] dimFilter = Utility.getDimFilter(json);
//             boolean isDimFilterBl = Utility.getIsDimFilterBl(json);
//             HashSet<BlockState> blockStateMatchers = Utils.getDefaultMatchers();
//             PlutonType type = PlutonType.valueOf(json.get("type").getAsString());
//             float density = json.get("density").getAsFloat();
//             boolean isWhitelist = json.get("isWhitelist").getAsBoolean();
//             List<BiomeDictionary.Type> biomeTypes = Utility.extractBiomeTypes(json.get("biomes").getAsJsonArray());
//             List<Biome> biomes = Utility.extractBiomes(json.get("biomes").getAsJsonArray());

//             if (json.has("blockStateMatchers")) {
//                 blockStateMatchers = Utility.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
//             }

//             return new DepositBiomeRestricted(block, sample, yMin, yMax, size, chance, dimFilter, isDimFilterBl,
//                     blockStateMatchers, biomes, biomeTypes, isWhitelist, type, density);
//         } catch (Exception e) {
//             Geolosys.getInstance().LOGGER.error("Failed to parse JSON file: {}", e);
//             return null;
//         }
//     }

//     private DepositMultiOreBiomeRestricted deserializeDepositMultiOreBiomeRestricted(JsonObject json) {
//         try {
//             HashMap<BlockState, Integer> blocks = Utility.buildMultiBlockMap(json.get("blocks").getAsJsonArray());
//             HashMap<BlockState, Integer> samples = Utility.buildMultiBlockMap(json.get("samples").getAsJsonArray());
//             int size = json.get("size").getAsInt();
//             int chance = json.get("chance").getAsInt();
//             int yMin = json.get("yMin").getAsInt();
//             int yMax = json.get("yMax").getAsInt();
//             String[] dimFilter = Utility.getDimFilter(json);
//             boolean isDimFilterBl = Utility.getIsDimFilterBl(json);
//             HashSet<BlockState> blockStateMatchers = Utils.getDefaultMatchers();
//             PlutonType type = PlutonType.valueOf(json.get("type").getAsString());
//             float density = json.get("density").getAsFloat();
//             boolean isWhitelist = json.get("isWhitelist").getAsBoolean();
//             List<BiomeDictionary.Type> biomeTypes = Utility.extractBiomeTypes(json.get("biomes").getAsJsonArray());
//             List<Biome> biomes = Utility.extractBiomes(json.get("biomes").getAsJsonArray());

//             if (json.has("blockStateMatchers")) {
//                 blockStateMatchers = Utility.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
//             }

//             return new DepositMultiOreBiomeRestricted(blocks, samples, yMin, yMax, size, chance, dimFilter,
//                     isDimFilterBl, blockStateMatchers, biomes, biomeTypes, isWhitelist, type, density);
//         } catch (Exception e) {
//             Geolosys.getInstance().LOGGER.error("Failed to parse JSON file: {}", e);
//             return null;
//         }
//     }
// }