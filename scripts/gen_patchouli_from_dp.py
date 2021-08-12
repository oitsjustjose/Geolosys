import os
import json
from typing import List

def to_proper(s: str) -> str:
    ret = []
    for x in s.split(" "):
        ret.append(x[0].upper() + x[1:])
    return " ".join(ret)

def strip_namespace(s: str) -> str:
    if ":" not in s:
        return s
    return s[s.index(":")+1:]

def fancy_join(l: List[str]) -> str:
    size = len(l) - 1
    
    if len(l) == 0:
        return l[0]

    ret = ""
    for idx, el in enumerate(l):
        ret += el
        if idx < size:
            if idx == size - 1:
                ret += " and "
            else:
                ret += ", "
    return ret

def get_name(config: dict) -> str:
    if "blocks" in config:
        blocks = list(map(lambda x: x["block"], config["blocks"]))
        blocks = list(map(lambda x: x.replace("_", " "), blocks))
        blocks = list(map(strip_namespace, blocks))
        blocks = list(map(to_proper, blocks))
        return fancy_join(blocks)
    else:
        return to_proper(strip_namespace(config["block"].replace("_", " ")))

def generate_dim_spec(config: dict) -> str:
    if "dimensions" in config:
        if config['dimensions']['isBlacklist'] == True:
            return f"cannot be found in the {', '.join(config['dimensions']['filter']).replace('the_', '')}"
        return f"can only be found in the {', '.join(config['dimensions']['filter']).replace('the_', '')}"
    return f"cannot be found in the {', '.join(config['dimBlacklist']).replace('the_', '')}"

def sign_depth(val: int) -> str:
    diff = 64 - val
    if diff > 0:
        return f"{abs(diff)} meters below"
    return f"{abs(diff)} meters above"

def get_biome_info(config: dict) -> str:
    if "biomes" in config:
        biomes = list(map(strip_namespace, config["biomes"]))
        biomes = list(map(lambda x: x.lower(), config["biomes"]))
        biomes = fancy_join(biomes)
        if "isWhitelist" in config:
            return f"This pluton is only found in {biomes} biomes"
        return f"This pluton is found anywhere except for {biomes} biomes"
    return "This pluton can be found in any biome"

def generate_ore(config: dict) -> dict:
    item = config["blocks"][0]["block"] if "blocks" in config else config["block"]
    name = get_name(config).replace(" Ore", "")
    ret = {
        "name": name,
        "icon": item,
        "category": "geolosys:03_resources",
        "pages": [
            {
                "type": "spotlight",
                "title": name,
                "item": item,
                "link_recipe": False,
                "text": f"This {' '.join(config['type'].lower().split('_'))} pluton generates between {sign_depth(config['yMax'])} and {sign_depth(config['yMin'])} sea level. It {generate_dim_spec(config)}. {get_biome_info(config)}."
            }
        ]
    }

    return ret

def generate_stone(config: dict) -> dict:
    item = config["block"]
    name = get_name(config)
    ret = {
        "name": name,
        "icon": item,
        "category": "geolosys:03_resources",
        "pages": [
            {
                "type": "spotlight",
                "title": name,
                "item": item,
                "link_recipe": False,
                "text": f"This stone pluton generates between {sign_depth(config['yMax'])} and {sign_depth(config['yMin'])} sea level. It {generate_dim_spec(config)}."
            }
        ]
    }

    return ret

for root, _, files in os.walk('../../../../../deposits'):
    for file in files:
        with open(f"{root}/{file}", "r") as fp:
            data = json.loads(fp.read())
        
        config = data["config"]
        output_d = None
        if data["type"] == "geolosys:deposit":
            output_d = generate_ore(config)
        else:
            output_d = generate_stone(config)

        with open(file, "w") as fp:
            fp.write(json.dumps(output_d))

            