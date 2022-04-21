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

    # if len(sys.argv) != 2:
    #     print("Expected one argument:")
    #     print("\tpython[3] generate.py <PATH_TO_BASE_LAYER>.png")
    #     return

    # background_fn = sys.argv[1]

    if not os.path.exists("./out"):
        os.mkdir("./out")

    for base_fn in os.listdir("./bases"):
        if not base_fn.endswith(".png"):
            continue

        background_name = base_fn[: base_fn.rindex(".")]
        background = Image.open(f"./bases/{base_fn}").convert("RGBA")

        for overlay_fn in os.listdir("./overlays"):
            try:
                if not overlay_fn.endswith(".png"):
                    continue

                overlay = Image.open(f"./overlays/{overlay_fn}")
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
