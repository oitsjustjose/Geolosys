import os
import sys
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
    return s[s.index(":") + 1 :]


def fancy_join(l: List[str], use_or=False) -> str:
    size = len(l) - 1

    if len(l) == 0:
        return l[0]

    ret = ""
    for idx, el in enumerate(l):
        ret += el
        if idx < size:
            if idx == size - 1:
                ret += " or " if use_or else " and "
            else:
                ret += ", "
    return ret


def get_name(config: dict) -> str:
    blocks = list(map(lambda x: x["block"], config["blocks"]["default"]))
    blocks = list(filter(lambda x: x is not None, blocks))
    blocks = list(map(lambda x: x.replace("_", " "), blocks))
    blocks = list(map(strip_namespace, blocks))
    blocks = list(map(to_proper, blocks))
    return fancy_join(blocks)


def generate_dim_spec(config: dict) -> str:
    if "dimensions" in config:
        if config["dimensions"]["isBlacklist"] == True:
            return f"cannot be found in the {fancy_join(config['dimensions']['filter'], use_or=True).replace('the_', '')}"
        return f"can only be found in the {fancy_join(config['dimensions']['filter'], use_or=True).replace('the_', '')}"
    return f"cannot be found in the {fancy_join(config['dimBlacklist']).replace('the_', '')}"


def sign_depth(val: int) -> str:
    diff = 64 - val
    if diff > 0:
        return f"{abs(diff)} meters below"
    return f"{abs(diff)} meters above"


def get_biome_info(config: dict) -> str:
    if "biomes" in config:
        biomes = list(map(strip_namespace, config["biomes"]["filter"]))
        biomes = list(map(lambda x: x.lower(), biomes))
        biomes = fancy_join(biomes)
        if config["biomes"]["isBlacklist"]:
            return f"is found anywhere except for {biomes} biomes"
        return f"is only found in {biomes} biomes"
    return "can be found in any biome"


def get_depth_info(data) -> str:
    try:
        if data["type"] == "geolosys:deposit_top_layer":
            samples = list(map(lambda x: x["block"], data["config"]["samples"]))
            samples = list(filter(lambda x: x is not None, samples))
            samples = list(map(lambda x: x.replace("_", " "), samples))
            samples = list(map(strip_namespace, samples))
            samples = list(map(to_proper, samples))
            samples = fancy_join(samples)
            return f"can be found directly on the surface with {samples} on top of it"
        return f"can be found between {sign_depth(data['config']['yMax'])} and {sign_depth(data['config']['yMin'])} sea level"
    except Exception as e:
        print(data, e)
        return ""


def gen(data: dict) -> dict:
    type: str = to_proper(data["type"].replace("geolosys:deposit_", ""))
    config: dict = data["config"]
    # Deprecated
    # item = config["blocks"][0]["block"] if "blocks" in config else config["block"]
    item = config["blocks"]["default"][0]["block"]
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
                "text": f"This {' '.join(type.lower().split('_'))} deposit {get_depth_info(data)}. It {generate_dim_spec(config)} and {get_biome_info(config)}.",
            }
        ],
    }

    return ret


if len(sys.argv) != 2:
    print("Usage: gen_patchouli_from_dp <DEPOSITS_DIR>")
    sys.exit(0)

for root, _, files in os.walk(sys.argv[1]):
    if not os.path.exists("./out"):
        os.mkdir("./out")
    for file in files:
        with open(f"{root}/{file}", "r") as fp:
            data = json.loads(fp.read())

        config = data
        output_d = gen(config)

        with open(f"./out/{file}", "w") as fp:
            fp.write(json.dumps(output_d))
