import sys
import os
try:
    from PIL import Image
except ImportError:
    print("Pillow is required. Install with:")
    print("\tpip[3] install pillow")
    sys.exit(0)

def main() -> None:
    """
    The main run-file for overlaying some image.
    Running:
        python[3] generate.py <PATH_TO_BASE_LAYER>.png
    """

    if len(sys.argv) != 2:
        print("Expected one argument:")
        print("\tpython[3] generate.py <PATH_TO_BASE_LAYER>.png")
        return
    
    background_fn = sys.argv[1]
    background_name = background_fn[:background_fn.rindex(".")]
    background = Image.open(background_fn)

    if not os.path.exists("./out"):
        os.mkdir("./out")

    for file in os.listdir("./overlays"):
        if not file.endswith(".png"):
            continue

        overlay = Image.open(f"./overlays/{file}")
        Image.alpha_composite(
            background, overlay
        ).save(f"./out/{background_name}_{file}")

if __name__ == "__main__":
    main()
