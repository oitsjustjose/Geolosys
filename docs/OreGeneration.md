# Ore Generation Changes

The default configuration for Ore Generation has changed with Geolosys v3.0.0. Many deposit configs remain unchanged, but a few have changed:

* Lapis is now found in Deserts of all types (hills and mutated)
* Beryl is now found in Extreme Hills of all types, and Ice Mountains
* Limonite is now found on Mushroom Islands and in Swamplands
* Azurite and Malachite now generate together, as in real life. Azurite generates at a 60% chance, where Malachite generates at a 40% chance.
* Cassiterite is now found in ocean biomes (may be changed)
* Teallite is now found in ocean biomes (may be changed) and has a small chance to generate with Beryl, as in real life.
* Galena is now found in flat biomes, such as Plains and Ice Plains
* Bauxite is now found in Jungle biomes and Savannah biomes
* Autunite is now found in Extreme Hills of all types, and Ice Mountains

All of these changes are reflected in the Field Manual

## Pluton Density

Additionally, plutons can now have a density (as of 3.0.0c). This density describes how much of the pluton is/are the defined ore block/blocks. For example, a density of `1.0` means the pluton will generate exactly how it used to. A density of `0.75` means that the pluton will take on the same shape, but only 75% of the blocks within that shape will actually be the ore block(s) -- the rest will remain whatever block they were initially.

This change is reflected in the docs for CraftTweaker and the JSON config.
