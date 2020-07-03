package com.oitsjustjose.geolosys.common.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.stream.JsonReader;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConfigOres {
    private File jsonFile;
    private ArrayList<PendingOre> pendingOres;
    private ArrayList<PendingStone> pendingStones;

    public ConfigOres(File configRoot) {
        this.jsonFile = new File(configRoot.getAbsolutePath() + "/geolosys.json");
        this.pendingOres = new ArrayList<>();
        this.pendingStones = new ArrayList<>();
    }

    /**
     * Runs after @EventHandler(FMLPostInitEvent)
     * 
     * Makes sure blocks not available in init are loaded now. If they're not, then
     * an error is displayed in the logs
     */
    public void postInit() {
        for (PendingOre pending : this.pendingOres) {
            if (register(pending)) {
                Geolosys.getInstance().LOGGER.info("Successfully registered JSON entry " + pending.oreBlocks.keySet()
                        + " **LATE**. This means that ore gen will work, but inter-mod compat may not");
            } else {
                Geolosys.getInstance().LOGGER.info("Failed to register JSON entry " + pending.oreBlocks.keySet()
                        + ". Please ensure that all entries are correct. This ore entry has been skipped completely");
            }
        }

        for (PendingStone pending : this.pendingStones) {
            if (register(pending)) {
                Geolosys.getInstance().LOGGER.info("Successfully registered JSON entry " + pending.stone
                        + " **LATE**. This means that stone gen will work, but inter-mod compat may not");
            } else {
                Geolosys.getInstance().LOGGER.info("Failed to register JSON entry " + pending.stone
                        + ". Please ensure that all entries are correct. This stone entry has been skipped completely");
            }
        }
    }

    public void init() {
        try {
            InputStream jsonStream = new FileInputStream(jsonFile);
            this.read(jsonStream);
        } catch (IOException e) {
            // Download the file from GitHub if it can't be found
            try {
                Geolosys.getInstance().LOGGER.info("Could not find geolosys.json. Downloading it from GitHub...");
                BufferedInputStream in = new BufferedInputStream(
                        new URL("https://raw.githubusercontent.com/oitsjustjose/Geolosys/1.12.x/geolosys_ores.json")
                                .openStream());
                Files.copy(in, Paths.get(jsonFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                Geolosys.getInstance().LOGGER.info("Done downloading geolosys.json from GitHub!");
                InputStream jsonStream = new FileInputStream(jsonFile);
                this.read(jsonStream);

            } catch (IOException f) {
                Geolosys.proxy.throwDownloadError(this.jsonFile);
            }
        }
    }

    private void read(InputStream in) throws IOException {
        JsonReader jReader = new JsonReader(new InputStreamReader(in));
        try {
            jReader.beginObject();
            while (jReader.hasNext()) {
                String name = jReader.nextName();
                if (name.equalsIgnoreCase("ores")) {
                    jReader.beginArray();
                    while (jReader.hasNext()) {
                        HashMap<String, Integer> oreBlocks = new HashMap<>();
                        HashMap<String, Integer> sampleBlocks = new HashMap<>();
                        int yMin = -1;
                        int yMax = -1;
                        int size = -1;
                        int chance = -1;
                        int[] dimBlacklist = new int[] {};
                        ArrayList<String> blockStateMatchers = new ArrayList<>();
                        ArrayList<Biome> biomes = new ArrayList<>();
                        ArrayList<BiomeDictionary.Type> biomeTypes = new ArrayList<>();
                        boolean isWhitelist = false;
                        boolean hasIsWhitelist = false;
                        float density = 1.0F;
                        String plutonName = new String();
                        jReader.beginObject();
                        while (jReader.hasNext()) {
                            String subName = jReader.nextName();
                            if (subName.equalsIgnoreCase("blocks")) {
                                jReader.beginArray();
                                while (jReader.hasNext()) {
                                    oreBlocks.put(jReader.nextString(), jReader.nextInt());
                                }
                                jReader.endArray();
                            } else if (subName.equalsIgnoreCase("samples")) {
                                jReader.beginArray();
                                while (jReader.hasNext()) {
                                    sampleBlocks.put(jReader.nextString(), jReader.nextInt());
                                }
                                jReader.endArray();
                            } else if (subName.equalsIgnoreCase("yMin")) {
                                yMin = jReader.nextInt();
                            } else if (subName.equalsIgnoreCase("yMax")) {
                                yMax = jReader.nextInt();
                            } else if (subName.equalsIgnoreCase("size")) {
                                size = jReader.nextInt();
                            } else if (subName.equalsIgnoreCase("chance")) {
                                chance = jReader.nextInt();
                            } else if (subName.equalsIgnoreCase("dimBlacklist")) {
                                ArrayList<Integer> tmp = new ArrayList<>();
                                jReader.beginArray();
                                while (jReader.hasNext()) {
                                    tmp.add(jReader.nextInt());
                                }
                                jReader.endArray();
                                dimBlacklist = fromArrayList(tmp);
                            } else if (subName.equalsIgnoreCase("blockStateMatchers")) {
                                jReader.beginArray();
                                while (jReader.hasNext()) {
                                    blockStateMatchers.add(jReader.nextString());
                                }
                                jReader.endArray();
                            } else if (subName.equalsIgnoreCase("biomes")) {
                                jReader.beginArray();
                                while (jReader.hasNext()) {
                                    String testFor = jReader.nextString();
                                    Biome b = ForgeRegistries.BIOMES.getValue(new ResourceLocation(testFor));
                                    if (b != null) {
                                        biomes.add(b);
                                    } else {
                                        for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.getAll()) {
                                            if (biomeType.getName().equalsIgnoreCase(testFor)) {
                                                biomeTypes.add(biomeType);
                                                break;
                                            }
                                        }
                                    }
                                }
                                jReader.endArray();
                            } else if (subName.equalsIgnoreCase("isWhitelist")) {
                                isWhitelist = jReader.nextBoolean();
                                hasIsWhitelist = true;
                            } else if (subName.equalsIgnoreCase("density")) {
                                density = (float) jReader.nextDouble();
                            } else if (subName.equalsIgnoreCase("name")) {
                                plutonName = (String) jReader.nextString();
                            } else {
                                Geolosys.getInstance().LOGGER
                                        .info("Unknown property found in geolosys_ores.json file. Skipping it.");
                                jReader.skipValue();

                            }
                        }
                        if (!register(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist,
                                blockStateMatchers, biomes, biomeTypes, isWhitelist, hasIsWhitelist, density,
                                plutonName)) {
                            this.pendingOres.add(new PendingOre(oreBlocks, sampleBlocks, yMin, yMax, size, chance,
                                    dimBlacklist, blockStateMatchers, biomes, biomeTypes, isWhitelist, hasIsWhitelist,
                                    density, plutonName));
                        }
                        jReader.endObject();
                    }
                    jReader.endArray();
                } else if (name.equalsIgnoreCase("stones")) {
                    jReader.beginArray();
                    while (jReader.hasNext()) {
                        jReader.beginObject();
                        String stone = null;
                        int yMin = -1;
                        int yMax = -1;
                        int chance = -1;
                        int size = -1;
                        int[] dimBlacklist = new int[] {};
                        while (jReader.hasNext()) {
                            String subName = jReader.nextName();
                            if (subName.equalsIgnoreCase("block")) {
                                stone = jReader.nextString();
                            } else if (subName.equalsIgnoreCase("yMin")) {
                                yMin = jReader.nextInt();
                            } else if (subName.equalsIgnoreCase("yMax")) {
                                yMax = jReader.nextInt();
                            } else if (subName.equalsIgnoreCase("size")) {
                                size = jReader.nextInt();
                            } else if (subName.equalsIgnoreCase("chance")) {
                                chance = jReader.nextInt();
                            } else if (subName.equalsIgnoreCase("dimBlacklist")) {
                                ArrayList<Integer> tmp = new ArrayList<>();
                                jReader.beginArray();
                                while (jReader.hasNext()) {
                                    tmp.add(jReader.nextInt());
                                }
                                jReader.endArray();
                                dimBlacklist = fromArrayList(tmp);
                            }
                        }
                        if (!register(stone, yMin, yMax, chance, size, dimBlacklist)) {
                            this.pendingStones.add(new PendingStone(stone, yMin, yMax, chance, size, dimBlacklist));
                        }
                        jReader.endObject();
                    }
                    jReader.endArray();
                } else {
                    jReader.skipValue();
                }
            }
            jReader.endObject();
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error(
                    "There was a parsing error with the geolosys_ores.json file. Please check for drastic syntax errors and check it at https://jsonlint.com/");
            Geolosys.getInstance().LOGGER.error(e.getMessage());
        } finally {
            jReader.close();
        }
    }

    /**
     * 
     * @param stone The Pending Stone to register
     * @return true if the registreation succeeded (i.e. no null blockstates); false
     *         otherwise
     */
    private boolean register(PendingStone stone) {
        return register(stone.stone, stone.yMin, stone.yMax, stone.chance, stone.size, stone.dimBlacklist);
    }

    /**
     * Registers a stone with the GeolosysAPI using the passed params
     * 
     * @param stone        The String form of the IBlockState
     * @param yMin         The minimum Y level this stone can generate
     * @param yMax         The maximum Y level this stone can generate
     * @param chance       The chance this stone can generate
     * @param size         The size this stone will be generated
     * @param dimBlacklist The list of dims the stone cannot generate
     * @return false if the state does not exist; true otherwise
     */
    private boolean register(String stone, int yMin, int yMax, int chance, int size, int[] dimBlacklist) {
        IBlockState state = fromString(stone);
        if (state == null) {
            return false;
        }
        GeolosysAPI.registerStoneDeposit(state, yMin, yMax, chance, size, dimBlacklist);
        return true;
    }

    /**
     * 
     * @param ore The Pending Ore to register
     * @return true if the registration succeeded (i.e. no null blockstates); false
     *         otherwise.
     */
    private boolean register(PendingOre ore) {
        return register(ore.oreBlocks, ore.sampleBlocks, ore.yMin, ore.yMax, ore.size, ore.chance, ore.dimBlacklist,
                ore.blockStateMatchers, ore.biomes, ore.biomeTypes, ore.isWhitelist, ore.hasIsWhitelist, ore.density,
                ore.plutonName);
    }

    /**
     * Registers an ore with the GeolosysAPI using the passed params
     * 
     * @param oreBlocks          A pair of String forms of an IBlockState of ores,
     *                           paried with their chance
     * @param sampleBlocks       A pair of String forms of an IBlockState of
     *                           samples, paired with their chance
     * @param yMin               The minimum Y level this ore can generate
     * @param yMax               The maximum Y level this ore can generate
     * @param size               The size this ore can generate to be
     * @param chance             The chance this ore can generate
     * @param dimBlacklist       The list of dims the ore cannot generate
     * @param blockStateMatchers A list of String forms of IBlockStates that this
     *                           ore can replace when genning
     * @param biomes             A list of Biomes that this ore can/cannot generate
     *                           in
     * @param isWhitelist        Whether or not the list of biomes is whitelist or
     *                           not
     * @param hasIsWhitelist     Whether or not the isWhitelist boolean had been
     *                           populated
     * @param density            The density (amount of ore vs. air/stone blocks)
     *                           this deposit has
     * @return true if successfully registered, false if any blockstates are null
     */
    private boolean register(HashMap<String, Integer> oreBlocks, HashMap<String, Integer> sampleBlocks, int yMin,
            int yMax, int size, int chance, int[] dimBlacklist, ArrayList<String> blockStateMatchers,
            ArrayList<Biome> biomes, List<BiomeDictionary.Type> biomeTypes, boolean isWhitelist, boolean hasIsWhitelist,
            float density, String plutonName) {
        HashMap<IBlockState, Integer> oreBlocksParsed = new HashMap<>();
        HashMap<IBlockState, Integer> sampleBlocksParsed = new HashMap<>();
        ArrayList<IBlockState> blockStateMatchersParsed = new ArrayList<>();

        for (Entry<String, Integer> e : oreBlocks.entrySet()) {
            IBlockState state = fromString(e.getKey());
            if (state == null) {
                return false;
            }
            oreBlocksParsed.put(state, e.getValue());
        }

        for (Entry<String, Integer> e : sampleBlocks.entrySet()) {
            IBlockState state = fromString(e.getKey());
            if (state == null) {
                return false;
            }
            sampleBlocksParsed.put(state, e.getValue());
        }

        for (String s : blockStateMatchers) {
            IBlockState state = fromString(s);
            if (state == null) {
                return false;
            }
            blockStateMatchersParsed.add(state);
        }

        if (biomes.size() > 0 || biomeTypes.size() > 0) {
            if (hasIsWhitelist) {
                GeolosysAPI.registerMineralDeposit(oreBlocksParsed, sampleBlocksParsed, yMin, yMax, size, chance,
                        dimBlacklist, (blockStateMatchers.size() == 0 ? null : blockStateMatchersParsed), biomes,
                        biomeTypes, isWhitelist, density, plutonName.length() > 0 ? plutonName : null);
            } else {
                Geolosys.getInstance().LOGGER.info(
                        "Received a biome list but no isWhitelist variable to define if the biome list is whitelist or blacklist.\n"
                                + "Registering it as a normal ore with no biome restrictions");
                GeolosysAPI.registerMineralDeposit(oreBlocksParsed, sampleBlocksParsed, yMin, yMax, size, chance,
                        dimBlacklist, (blockStateMatchers.size() == 0 ? null : blockStateMatchersParsed), density, plutonName.length() > 0 ? plutonName : null);
            }
        } else {
            GeolosysAPI.registerMineralDeposit(oreBlocksParsed, sampleBlocksParsed, yMin, yMax, size, chance,
                    dimBlacklist, (blockStateMatchers.size() == 0 ? null : blockStateMatchersParsed), density, plutonName.length() > 0 ? plutonName : null);
        }
        Geolosys.getInstance().LOGGER.info("Registered " + oreBlocks + ", " + sampleBlocks + " with density " + density
                + ". " + ((biomeTypes.size() > 0 || biomes.size() > 0) ? "This ore has custom biome registries" : ""));
        return true;
    }

    private IBlockState fromString(String iBlockState) {
        String[] parts = iBlockState.split(":");
        if (parts.length == 2) {
            Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
            return b.getDefaultState();
        } else if (parts.length == 3) {
            Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
            return Utils.getStateFromMeta(b, Integer.parseInt(parts[2]));
        } else {
            return null;
        }
    }

    private int[] fromArrayList(ArrayList<Integer> arrList) {
        int[] retVal = new int[arrList.size()];
        for (int i = 0; i < arrList.size(); i++) {
            retVal[i] = arrList.get(i);
        }
        return retVal;
    }

    private static class PendingOre {
        public HashMap<String, Integer> oreBlocks;
        public HashMap<String, Integer> sampleBlocks;
        public int yMin;
        public int yMax;
        public int size;
        public int chance;
        public int[] dimBlacklist;
        public ArrayList<String> blockStateMatchers;
        public ArrayList<Biome> biomes;
        public List<BiomeDictionary.Type> biomeTypes;
        public boolean isWhitelist;
        public boolean hasIsWhitelist;
        public float density;
        public String plutonName;

        public PendingOre(HashMap<String, Integer> oreBlocks, HashMap<String, Integer> sampleBlocks, int yMin, int yMax,
                int size, int chance, int[] dimBlacklist, ArrayList<String> blockStateMatchers, ArrayList<Biome> biomes,
                List<BiomeDictionary.Type> biomeTypes, boolean isWhitelist, boolean hasIsWhitelist, float density,
                String plutonName) {
            this.oreBlocks = oreBlocks;
            this.sampleBlocks = sampleBlocks;
            this.yMin = yMin;
            this.yMax = yMax;
            this.size = size;
            this.chance = chance;
            this.dimBlacklist = dimBlacklist;
            this.blockStateMatchers = blockStateMatchers;
            this.biomes = biomes;
            this.biomeTypes = biomeTypes;
            this.isWhitelist = isWhitelist;
            this.hasIsWhitelist = hasIsWhitelist;
            this.density = density;
            this.plutonName = plutonName;
        }
    }

    private static class PendingStone {
        public String stone;
        public int yMin;
        public int yMax;
        public int chance;
        public int size;
        public int[] dimBlacklist;

        public PendingStone(String stone, int yMin, int yMax, int chance, int size, int[] dimBlacklist) {
            this.stone = stone;
            this.yMin = yMin;
            this.yMax = yMax;
            this.chance = chance;
            this.size = size;
            this.dimBlacklist = dimBlacklist;
        }
    }
}
