"""
    Author: Jose Stovall
    Geolosys: https://github.com/oitsjustjose/Geolosys
"""
import json

from pip._vendor.colorama import Fore


def generate_drops(drop_map: dict) -> None:
    for ore in drop_map:
        json_doc = {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [
                        {
                            "type": "minecraft:item",
                            "name": drop_map[ore]
                        }
                    ],
                    "conditions": [
                        {
                            "condition": "minecraft:survives_explosion"
                        }
                    ]
                }
            ]
        }
        with open("./geolosys/loot_tables/blocks/{}.json".format(ore.replace("geolosys:", "")), "w") as file:
            file.write(json.dumps(json_doc))
        print(Fore.BLUE + "Sucessfully wrote ./geolosys/loot_tables/blocks/{}.json".format(
            ore.replace("geolosys:", "")) + Fore.RESET)


def main() -> None:
    drop_map = {
        "geolosys:autunite_ore": "geolosys:uranium_cluster",
        "geolosys:azurite_ore": "geolosys:copper_cluster",
        "geolosys:bauxite_ore": "geolosys:aluminum_cluster",
        "geolosys:cassiterite": "geolosys:tin_cluster",
        "geolosys:gold_ore": "geolosys:gold_cluster",
        "geolosys:hematite_ore": "geolosys:iron_cluster",
        "geolosys:malachite_ore": "geolosys:copper_cluster",
        "geolosys:platinum_ore": "geolosys:platinum_cluster",
        "geolosys:sphalerite_ore": "geolosys:zinc_cluster",
        "geolosys:teallite_ore": "geolosys:tin_cluster"
    }
    generate_drops(drop_map)


if __name__ == "__main__":
    main()
