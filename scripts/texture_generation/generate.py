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

    if not os.path.exists("./out"):
        os.mkdir("./out")

    if not os.path.exists("./base_layers"):
        print("Base Layers folder not found!")
        print(
            "Please place your Base Layer Textures in a new folder adjacent to this file named `base_layers`"
        )
        sys.exit(0)
    if not os.path.exists("./overlay_layers"):
        print("Overlay Layers folder not found!")
        print(
            "Please place your Overlay Textures in a new folder adjacent to this file named `overlay_layers`"
        )

        sys.exit(0)

    for base_fn in os.listdir("./base_layers"):
        if not base_fn.endswith(".png"):
            continue

        background_name = base_fn[: base_fn.rindex(".")]
        background = Image.open(f"./base_layers/{base_fn}").convert("RGBA")

        for overlay_fn in os.listdir("./overlay_layers"):
            try:
                if not overlay_fn.endswith(".png"):
                    continue

                overlay = Image.open(f"./overlay_layers/{overlay_fn}")
                Image.alpha_composite(background, overlay).save(
                    f"./out/{background_name}_{overlay_fn}"
                )
            except ValueError:
                print(overlay_fn)
                print(overlay.mode)
                print(background.mode)
                return


if __name__ == "__main__":
    main()
