import json


def generate(mapping: dict) -> None:
    """Generates JSON files w/ forge conditions"""
    for cluster in mapping:
        smelting = {"type": "forge:conditional", "recipes": [{"conditions": [
            {"values": [{"item": "{}".format(mapping[cluster]), "type": "forge:item_exists"}], "type": "forge:and"}],
            "recipe": {"type": "minecraft:smelting",
                       "ingredient": {"item": "{}".format(cluster)},
                       "result": "{}".format(mapping[cluster]),
                       "experience": 0.7, "cookingtime": 200}}]}
        blasting = {"type": "forge:conditional", "recipes": [{"conditions": [
            {"values": [{"item": "{}".format(mapping[cluster]), "type": "forge:item_exists"}], "type": "forge:and"}],
            "recipe": {"type": "minecraft:blasting",
                       "ingredient": {"item": "{}".format(cluster)},
                       "result": "{}".format(mapping[cluster]),
                       "experience": 0.7, "cookingtime": 100}}]}
        with open("./geolosys/recipes/{}_smelting.json".format(
                mapping[cluster].replace("geolosys:", "").replace("minecraft:", "")), "w") as file:
            file.write(json.dumps(smelting))
        with open("./geolosys/recipes/{}_blasting.json".format(
                mapping[cluster].replace("geolosys:", "").replace("minecraft:", "")), "w") as file:
            file.write(json.dumps(blasting))


if __name__ == "__main__":
    mapping = {
        "geolosys:aluminum_cluster": "geolosys:aluminum_ingot",
        "geolosys:copper_cluster": "geolosys:copper_ingot",
        "geolosys:gold_cluster": "minecraft:gold_ingot",
        "geolosys:iron_cluster": "minecraft:iron_ingot",
        "geolosys:lead_cluster": "geolosys:lead_ingot",
        "geolosys:nickel_cluster": "geolosys:nickel_ingot",
        "geolosys:platinum_cluster": "geolosys:platinum_ingot",
        "geolosys:silver_cluster": "geolosys:silver_ingot",
        "geolosys:tin_cluster": "geolosys:tin_ingot",
        "geolosys:zinc_cluster": "geolosys:zinc_ingot",
    }
    generate(mapping)
