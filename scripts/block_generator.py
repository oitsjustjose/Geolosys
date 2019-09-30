"""
    Author: Jose Stovall
    Geolosys: https://github.com/oitsjustjose/Geolosys
"""
import json
import sys

from pip._vendor.colorama import Fore


def generate_states(states: list) -> None:
    for state in states:
        json_doc = {"variants": {"": [{"model": "geolosys:block/{}".format(state)}]}}
        with open("./blockstates/{}.json".format(state), "w") as file:
            file.write(json.dumps(json_doc))
            print(
                    Fore.BLUE
                    + "Wrote ./blockstates/{}.json to disk".format(state)
                    + Fore.RESET
            )


def generate_item_models(states: list) -> None:
    for state in states:
        json_doc = {
            "parent": "block/cube_all",
            "textures": {"all": "geolosys:blocks/{}".format(state)},
        }
        with open("./models/block/{}.json".format(state), "w") as file:
            file.write(json.dumps(json_doc))
            print(Fore.BLUE + "Wrote ./models/block/{}.json to disk".format(state) + Fore.RESET)


def generate_block_models(states: list) -> None:
    for state in states:
        json_doc = {"parent": "geolosys:block/{}".format(state)}
        with open("./models/item/{}.json".format(state), "w") as file:
            file.write(json.dumps(json_doc))
            print(Fore.BLUE + "Wrote ./models/item/{}.json to disk".format(state) + Fore.RESET)


def main(block_state: bool, item_model: bool, block_model: bool) -> None:
    """
    The main program logic which generates blockstates based on the array below
    Args:
        block_state (bool): trigger whether or not to generate blockstates
        item_model (bool): trigger whether or not to generate item models
        block_model (bool): trigger whether or not to generate block models
    """
    states = [
        "hematite_ore",
        "limonite_ore",
        "malachite_ore",
        "azurite_ore",
        "cassiterite_ore",
        "teallite_ore",
        "galena_ore",
        "bauxite_ore",
        "platinum_ore",
        "autunite_ore",
        "sphalerite_ore",
        "coal_ore",
        "cinnabar_ore",
        "gold_ore",
        "lapis_ore",
        "quartz_ore",
        "kimberlite_ore",
        "beryl_ore",
    ]

    if block_state:
        generate_states(states)
    if item_model:
        generate_item_models(states)
    if block_model:
        generate_block_models(states)


if __name__ == "__main__":

    def print_help() -> None:
        """Prints the help prompt for the user to know their options"""
        print(Fore.YELLOW + "Possible arguments:" + Fore.RESET)
        print(Fore.CYAN + "    -i: Generate Item Models" + Fore.RESET)
        print(Fore.CYAN + "    -b: Generate Block Models" + Fore.RESET)
        print(Fore.CYAN + "    -s: Generate BlockStates" + Fore.RESET)
        print(Fore.CYAN + "    -a: Generate All" + Fore.RESET)
        print(Fore.CYAN + "    -h, ? : This screen" + Fore.RESET)


    possible_args = {
        "-a": False,
        "-i": False,
        "-b": False,
        "-s": False,
        "-h": False,
        "?": False,
    }

    for arg in sys.argv[1:]:
        for arg_name in possible_args:
            if arg_name in arg:
                possible_args[arg_name] = True
    if not any(list(possible_args.values())):
        print(Fore.RED + "No arguments given." + Fore.RESET)
        print_help()
        exit()

    if possible_args["-h"] or possible_args["?"]:
        print_help()
        exit()

    if possible_args["-a"]:
        main(True, True, True)
    else:
        main(possible_args["-s"], possible_args["-i"], possible_args["-b"])
