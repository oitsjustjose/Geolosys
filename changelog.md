1.5:

**WARNING: You should disable 'cascading chunk lag' logging in the Forge Config. More explained below**

* ADDED: ExU2 Compatibility for Cinnabar!
* REMOVED: Config for custom ores... if this is chaos, let me know, but I decided to remove it ultimately because the samples wouldn't ever be able to be dynamic like that..
* CHANGED: Mineral deposits below Y=24 now generate magma blocks instead of lava to prevent frustration and be more geologically accurate
* CHANGED: Mineral deposits of lesser minerals (cassiterite, malachite & hematite) generate in smaller clusters by default (delete config entries to reset them!)
* CHANGED: Mineral deposits now generate... differently. Still in the same chunk and the same formation, but at . It's bound to cause "cascading chunk lag", but I've tested this extensively and it **does not progress for long** before stopping :)
* FIXED: Some chunks not generating mineral deposits even though there was a sample
* FIXED: Samples on samples
* CHANGED: Some new textures by Wiiv! When the rest are done, a new version will be released :D


1.4:
* ADDED: Config options for vanilla ores
* CHANGED: Vanilla ore rate defaults... again
* CHANGED: Diamonds are now called "Kimberlite"
* CHANGED: Diamond texture to be more Kimberlite-y
* FIXED: A few bad names in the configs... best to double check them

1.3:
**Warning, this is basically the 'Samples' update :P**

* ADDED: Right click interaction to samples
* FIXED: Samples not always generating
* FIXED: Samples spawning in superflat
* FIXED: Unused config option for # of veins per chunk (obviously it's 1, always)
* CHANGED: Samples generate in clusters of 1 to 4
* CHANGED: Samples behave more realistically
* CHANGED: Tweaked vanilla ore-gen values one last time. 5/7 are balanced now ;)

1.2:

* ADDED: Custom vanilla ore variants - these can be silk touched for the Vanilla ores. All drop values are directly referenced from their vanilla variants
* ADDED: Assorted Quartz Clusters can drop Certus Quartz, Charged Certus Quartz or Black Quartz if available.
* ADDED: Ore Samples on the surface above where that type of ore will generate. Mining within the chunk you find one will lead you to a deposit.
* ADDED: Separate user entries in the config for custom "stones" / "rocks"
* CHANGED: Mineral deposits found below Y = 24 can contain pockets of magma
* CHANGED: Rarities - a lot - only one ore type can generate per chunk
* CHANGED: Cluster Sizes - since ores are rarer, these are much larger
* CHANGED: Stone variants have their own separate chances to spawn, preventing them taking an OreGen oppportunity
* CHANGED: Minor refactors to be more similar to vanilla
* FIXED: Config GUI not prompting user to restart their game
* FIXED: Various smelting recipe issues
* FIXED: Rocks replacing more valuable minerals

1.1-alpha:

* ADDED: User configurable plutons entries (ideal for mods that I didn't include)
* CHANGED: Vanilla ore gen weights. Most things are more rare, except the stone variants; those are more common
* CHANGED: Default ore gen options. Materials with 2 variants are half as rare now.

1.0-alpha:

* Initial Release
