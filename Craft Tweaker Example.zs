import mods.geolosys.ores;   /* Import ores module */
import mods.geolosys.stones; /* Import stones module */

/* Let's start by adding an Ore */
/* The arguments are as follow;
    - <blockstate>: The ore block
    - <blockstate>: The sample block
    - int yMin
    - int yMax
    - int size
    - int chance: Higher means more likely; this is out of 100
    - int[] dimBlacklist: Array of dimension ID's the ore is NOT allowed in
*/
mods.geolosys.ores.addOre(<blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=oak>, 0, 70, 20, 20, [-1, 1]);

/* Want this ore to only specifically replace certain blocks? Use this command instead */
/* The arguments are as follow;
    - <blockstate>: The ore block
    - <blockstate>: The sample block
    - int yMin
    - int yMax
    - int size
    - int chance: Higher means more likely; this is out of 100
    - int[] dimBlacklist: Array of dimension ID's the ore is NOT allowed in
    - <blockstate>[]: Array of <blockstates> that the block can replace
*/
mods.geolosys.ores.addOre(<blockstate:minecraft:log:variant=jungle>, <blockstate:minecraft:log:variant=birch>, 23, 32, 10, 20, [-1, 1], [<blockstate:minecraft:stone>]);


/* Want to add a stone for generation? Ok, do that here: */
/* The arguments are as follow:
    - <blockstate>: The stone block
    - int yMin
    - int yMax
    - int chance: Higher means more likely; this is out of 100

    **SIZE IS FIXED**
*/
mods.geolosys.stones.addStone(<blockstate:minecraft:diamond_block>, 13, 100, 30);