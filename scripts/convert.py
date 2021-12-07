import os
import json

EXCLUDE = ["gold_nether.json", "ancient_debris.json", "peat.json", "andesite.json", "diorite.json", "granite.json"]

def deepslatify(old_resloc: str) -> str:
    nmsp, path = old_resloc.split(":")
    return f"{nmsp}:deepslate_{path}"

def main() -> None:
    for root, _, files in os.walk("./"):
        if files:
            for fn in list(filter(lambda x: x.endswith(".json") and x not in EXCLUDE, files)):
                with open(f"{root}/{fn}", "r") as fh:
                    data = json.loads(fh.read())
                
                config = data["config"]
                new_blocks = { "default": config["blocks"], "deepslate": [] }

                for pair in config["blocks"]:
                    block = pair["block"]
                    if block:
                        block = deepslatify(block)
                    new_blocks["deepslate"].append({
                        "block": block,
                        "chance": pair["chance"]
                    })

                data["config"]["blocks"] = new_blocks

                with open(f"{root}/{fn}", "w") as fh:
                    fh.write(json.dumps(data, indent=2))
                print(f"Converted {root}/{fn} to new format")


if __name__ == "__main__":
    main()