"""
    Author: Jose Stovall
    Geolosys: https://github.com/oitsjustjose/Geolosys
"""
import json
import sys

from pip._vendor.colorama import Fore


def generate_item_models(items: list) -> None:
    for item in items:
        json_doc = {
            "parent": "item/generated",
            "textures": {
                "layer0": "geolosys:items/{}".format(item)
            }
        }
        with open("./models/item/{}.json".format(item), "w") as file:
            file.write(json.dumps(json_doc))
            print(Fore.BLUE + "Wrote ./models/item/{}.json to disk".format(item) + Fore.RESET)


def main() -> None:
    """
    The main program logic which generates items based on the array below
    """
    items = [
        "lignite_coal_coke",
        "bituminous_coal_coke",
        "peat_coal",
        "lignite_coal",
        "bituminous_coal",
        "anthracite_coal",
        "copper_ingot",
        "tin_ingot",
        "silver_ingot",
        "lead_ingot",
        "aluminum_ingot",
        "nickel_ingot",
        "platinum_ingot",
        "zinc_ingot",
        "iron_cluster",
        "gold_cluster",
        "copper_cluster",
        "tin_cluster",
        "silver_cluster",
        "lead_cluster",
        "aluminum_cluster",
        "nickel_cluster",
        "platinum_cluster",
        "uranium_cluster",
        "zinc_cluster",
        "yellorium_cluster",
        "osmium_cluster"
    ]

    generate_item_models(items)


if __name__ == "__main__":

    def print_help() -> None:
        """Prints the help prompt for the user to know their options"""
        print(Fore.YELLOW + "Possible arguments:" + Fore.RESET)
        print(Fore.CYAN + "    -h, ? : This screen" + Fore.RESET)


    possible_args = {
        "-h": False,
        "?": False,
    }

    for arg in sys.argv[1:]:
        for arg_name in possible_args:
            if arg_name in arg:
                possible_args[arg_name] = True

    if possible_args["-h"] or possible_args["?"]:
        print_help()
        exit()

    main()
