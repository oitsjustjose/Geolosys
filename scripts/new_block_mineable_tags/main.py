import os
import json

VALUES = [(1, "lignite_ore"), (1, "bituminous_coal_ore"), (2, "anthracite_coal_ore"), (0, "coal_ore"), (2, "cinnabar_ore"), (2, "gold_ore"), (1, "lapis_ore"), (1, "quartz_ore"), (2, "kimberlite_ore"), (2, "beryl_ore"), (1, "nether_gold_ore"), (3, "ancient_debris_ore"), (1, "hematite_ore"), (2, "limonite_ore"), (1, "malachite_ore"), (2, "azurite_ore"), (1, "cassiterite_ore"), (2, "teallite_ore"), (2, "galena_ore"), (0, "bauxite_ore"), (2, "platinum_ore"), (2, "autunite_ore"), (1, "sphalerite_ore"), (1, "deepslate_lignite_ore"), (1, "deepslate_bituminous_coal_ore"), (2, "deepslate_anthracite_coal_ore"), (0, "deepslate_coal_ore"), (2, "deepslate_cinnabar_ore"), (2, "deepslate_gold_ore"), (1, "deepslate_lapis_ore"), (1, "deepslate_quartz_ore"), (2, "deepslate_kimberlite_ore"), (2, "deepslate_beryl_ore"), (1, "deepslate_hematite_ore"), (2, "deepslate_limonite_ore"), (1, "deepslate_malachite_ore"), (2, "deepslate_azurite_ore"), (1, "deepslate_cassiterite_ore"), (2, "deepslate_teallite_ore"), (2, "deepslate_galena_ore"), (0, "deepslate_bauxite_ore"), (2, "deepslate_platinum_ore"), (2, "deepslate_autunite_ore"), (1, "deepslate_sphalerite_ore")]
MAP_TOOLLVL_TO_JSON_NAME = {
    0: None,
    1: "needs_stone_tool.json", 
    2: "needs_iron_tool.json",
    3: "needs_diamond_tool.json"
}

def main() -> None:
    if not os.path.exists("./mineable"):
        os.mkdir("./mineable")

    mineable_pickaxe = { "replace": False, "values": [x[1] for x in VALUES] }
    with open("./mineable/pickaxe.json", "w") as fh:
        fh.write(json.dumps(mineable_pickaxe, indent=2))

    for lvl, nm in VALUES:
        fn = MAP_TOOLLVL_TO_JSON_NAME[lvl]
        if not fn: continue
        
        data = { "replace": False, "values": [] }
        if os.path.exists(fn):
            with open(fn, "r") as fh:
                data = json.loads(fh.read())
        
        data["values"].append(nm)
        with open(fn, "w") as fh:
            fh.write(json.dumps(data, indent=2))

    

if __name__ == "__main__":
    main()