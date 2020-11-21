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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.stream.JsonReader;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.PlutonType;
import com.oitsjustjose.geolosys.api.world.Deposit;
import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositStone;
import com.oitsjustjose.geolosys.api.world.IDeposit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class OreConfig {
    private static OreConfig instance;

    private File jsonFile;

    public OreConfig(File configRoot) {
        this.jsonFile = new File(configRoot.getAbsolutePath() + "/geolosys.json");
    }

    public static void setup(File configRoot) {
        instance = new OreConfig(configRoot);
    }

    public static OreConfig getInstance() {
        if (instance == null) {
            throw new RuntimeException("Geolosys OreConfig has not yet been initialized!");
        }
        return instance;
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
                        new URL("https://raw.githubusercontent.com/oitsjustjose/Geolosys/1.14.x/geolosys_ores.json")
                                .openStream());
                Files.copy(in, Paths.get(jsonFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                Geolosys.getInstance().LOGGER.info("Done downloading geolosys.json from GitHub!");
                InputStream jsonStream = new FileInputStream(jsonFile);
                this.read(jsonStream);

            } catch (IOException f) {
                Geolosys.getInstance().LOGGER.warn(f.getLocalizedMessage());
                Geolosys.proxy.throwDownloadError(this.jsonFile);
            }
        }
    }

    private void read(InputStream in) throws IOException {
        try (JsonReader jReader = new JsonReader(new InputStreamReader(in))) {
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
                        String[] dimBlacklist = new String[] {};
                        ArrayList<String> blockStateMatchers = new ArrayList<>();
                        ArrayList<Biome> biomes = new ArrayList<>();
                        ArrayList<BiomeDictionary.Type> biomeTypes = new ArrayList<>();
                        boolean isWhitelist = false;
                        boolean hasIsWhitelist = false;
                        PlutonType plutonType = null;
                        float density = 1.0F;
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
                                ArrayList<String> tmp = new ArrayList<>();
                                jReader.beginArray();
                                while (jReader.hasNext()) {
                                    tmp.add(jReader.nextString());
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
                                    Biome b = ForgeRegistries.BIOMES
                                            .getValue(new ResourceLocation(testFor.toLowerCase()));
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
                            } else if (subName.equalsIgnoreCase("type")) {
                                String temp = jReader.nextString();
                                try {
                                    plutonType = PlutonType.valueOf(temp.toUpperCase());
                                } catch (IllegalArgumentException e) {
                                    Geolosys.getInstance().LOGGER.info(
                                            "The pluton type {} is not valid. Your possible choices are:\n" + "{}\n"
                                                    + "Geolosys has chosen to use DENSE until this error is fixed",
                                            temp, Arrays.toString(PlutonType.values()));

                                    plutonType = PlutonType.DENSE;
                                }
                            } else if (subName.equalsIgnoreCase("density")) {
                                density = (float) jReader.nextDouble();
                            } else {
                                Geolosys.getInstance().LOGGER
                                        .info("Unknown property found in geolosys_ores.json file. Skipping it.");
                                jReader.skipValue();

                            }
                        }
                        if (!register(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist,
                                blockStateMatchers, biomes, biomeTypes, isWhitelist, hasIsWhitelist, plutonType,
                                density)) {
                            Geolosys.getInstance().LOGGER.info("Could not register pluton " + oreBlocks
                                    + " due to some error. Please narrow down which block is not being registered on time by narrowing them down one-by-one.");
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
                        String[] dimBlacklist = new String[] {};
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
                                ArrayList<String> tmp = new ArrayList<>();
                                jReader.beginArray();
                                while (jReader.hasNext()) {
                                    tmp.add(jReader.nextString());
                                }
                                jReader.endArray();
                                dimBlacklist = fromArrayList(tmp);
                            }
                        }
                        if (!register(stone, yMin, yMax, chance, size, dimBlacklist)) {
                            Geolosys.getInstance().LOGGER.info("Could not register pluton " + stone
                                    + " due to some error. Please report to the mod author that they are not initializing their blocks when they should..");
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
            e.printStackTrace();
        }
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
    private boolean register(String stone, int yMin, int yMax, int chance, int size, String[] dimBlacklist) {
        BlockState state = fromString(stone);
        if (state == null) {
            return false;
        }
        GeolosysAPI.plutonRegistry.addStonePluton(new DepositStone(state, yMin, yMax, chance, size, dimBlacklist));

        Geolosys.getInstance().LOGGER.info("Registered a stone pluton of {}.", stone);

        return true;
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
            int yMax, int size, int chance, String[] dimBlacklist, ArrayList<String> blockStateMatchers,
            ArrayList<Biome> biomes, List<BiomeDictionary.Type> biomeTypes, boolean isWhitelist, boolean hasIsWhitelist,
            PlutonType type, float density) {
        HashMap<BlockState, Integer> oreBlocksParsed = new HashMap<>();
        HashMap<BlockState, Integer> sampleBlocksParsed = new HashMap<>();
        ArrayList<BlockState> blockStateMatchersParsed = new ArrayList<>();

        IDeposit toRegister = null;

        // Validate every oreBlock item
        for (Entry<String, Integer> e : oreBlocks.entrySet()) {
            BlockState state = fromString(e.getKey());
            if (state == null) {
                return false;
            }
            oreBlocksParsed.put(state, e.getValue());
        }

        // Validate every sampleBlock item
        for (Entry<String, Integer> e : sampleBlocks.entrySet()) {
            BlockState state = fromString(e.getKey());
            if (state == null) {
                return false;
            }
            sampleBlocksParsed.put(state, e.getValue());
        }

        // Validate every blockStateMatcher
        for (String s : blockStateMatchers) {
            BlockState state = fromString(s);
            if (state == null) {
                return false;
            }
            blockStateMatchersParsed.add(state);
        }

        // Ensure that if biomes are specified, whether or not it's a whitelist is also
        // specified
        if ((biomes.size() > 0 || biomeTypes.size() > 0) && !hasIsWhitelist) {
            return false;
        }

        // Ensure that the PlutonType is declared
        if (type == null) {
            return false;
        }

        // Nullify an empty blockStateMatcher so that the default is used.
        if (blockStateMatchersParsed.size() <= 0) {
            blockStateMatchersParsed = null;
        }

        // Register as some variant of DepositMultiOre
        if (oreBlocks.size() > 0 || sampleBlocks.size() > 0) {
            if (biomes.size() > 0 || biomeTypes.size() > 0) {
                toRegister = new DepositMultiOreBiomeRestricted(oreBlocksParsed, sampleBlocksParsed, yMin, yMax, size,
                        chance, dimBlacklist, blockStateMatchersParsed, biomes, biomeTypes, isWhitelist, type, density);
            } else {
                toRegister = new DepositMultiOre(oreBlocksParsed, sampleBlocksParsed, yMin, yMax, size, chance,
                        dimBlacklist, blockStateMatchersParsed, type, density);
            }
        } else {
            if (biomes.size() > 0 || biomeTypes.size() > 0) {
                for (BlockState b : oreBlocksParsed.keySet()) {
                    for (BlockState s : sampleBlocksParsed.keySet()) {
                        toRegister = new DepositBiomeRestricted(b, s, yMin, yMax, size, chance, dimBlacklist,
                                blockStateMatchersParsed, biomes, biomeTypes, isWhitelist, type, density);
                        break;
                    }
                    break;
                }
            } else {
                for (BlockState b : oreBlocksParsed.keySet()) {
                    for (BlockState s : sampleBlocksParsed.keySet()) {
                        toRegister = new Deposit(b, s, yMin, yMax, size, chance, dimBlacklist, blockStateMatchersParsed,
                                type, density);
                        break;
                    }
                    break;
                }
            }
        }

        Geolosys.getInstance().LOGGER.info(
                "Registered a {} ore pluton of blocks={}, samples={}, and density={}. This ore {} custom biome registries.",
                type.toString().toLowerCase(), oreBlocks, sampleBlocks, density,
                (biomeTypes.size() > 0 || biomes.size() > 0) ? "has" : "does not have");

        return toRegister != null && GeolosysAPI.plutonRegistry.addOrePluton(toRegister);
    }

    private BlockState fromString(String iBlockState) {
        String[] parts = iBlockState.split(":");
        if (parts.length == 2) {
            Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
            return b.getDefaultState();
        }
        return null;
    }

    private String[] fromArrayList(ArrayList<String> arrList) {
        String[] ret = new String[arrList.size()];
        for (int i = 0; i < arrList.size(); i++) {
            ret[i] = arrList.get(i);
        }
        return ret;
    }

}