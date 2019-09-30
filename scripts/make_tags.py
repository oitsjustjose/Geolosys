"""
    Author: Jose Stovall
    Geolosys: https://github.com/oitsjustjose/Geolosys
"""
import json

from pip._vendor.colorama import Fore


def generate_ingots(ingots: list) -> None:
    """"""
    ingots_doc = {"replace": False, "values": []}
    for ingot in ingots:
        ingots_doc["values"].append("#forge:ingots/{}".format(ingot))
        json_doc = {"replace": False, "values": ["geolosys:{}_ingot".format(ingot)]}
        with open("./forge/tags/items/ingots/{}.json".format(ingot), 'w') as file:
            file.write(json.dumps(json_doc))
        print(Fore.BLUE + "Successfully wrote ./forge/tags/items/ingots/{}.json".format(ingot) + Fore.RESET)
    with open("./forge/tags/items/ingots.json", 'w') as file:
        file.write(json.dumps(ingots_doc))
    print(Fore.BLUE + "Successfully wrote ./forge/tags/items/ingots.json" + Fore.RESET)


def generate_clusters(clusters: list) -> None:
    """"""
    clusters_doc = {"replace": False, "values": []}

    for cluster in clusters:
        clusters_doc["values"].append("#forge:ores/{}".format(cluster))
        json_doc = {"replace": False, "values": ["geolosys:{}_cluster".format(cluster)]}
        with open("./forge/tags/items/ores/{}.json".format(cluster), 'w') as file:
            file.write(json.dumps(json_doc))

    with open("./forge/tags/items/ores.json", 'w') as file:
        file.write(json.dumps(clusters_doc))


def main() -> None:
    ingots = [
        "copper",
        "tin",
        "silver",
        "lead",
        "aluminum",
        "nickel",
        "platinum",
        "zinc"
    ]
    clusters = [
        "iron",
        "gold",
        "copper",
        "tin",
        "silver",
        "lead",
        "aluminum",
        "nickel",
        "platinum",
        "uranium",
        "zinc",
        "yellorium",
        "osmium"
    ]
    generate_ingots(ingots)
    generate_clusters(clusters)


if __name__ == "__main__":
    main()
