![Logo](https://raw.githubusercontent.com/oitsjustjose/Geolosys/master/Geolosys%20Logo.png)

Geolosys is a modpack-friendly version of TerraFirmaCraft ore generation. In essence, it is a simple mod that adds geological systems to Minecraft's world generation. It comes with an extensive config file (and GUI), and allows you to modify WorldGen greatly! With Geolosys, realistic mineral versions of common ores are added for many variants commonly used in modpacks.

Compatibility:

- Vanilla Stones (Diorite, Andesite, Granite)
- Vanilla Resources (Iron, Gold, Diamond, Emerald, Redstone, Coal, Lapis and Quartz)
- Common Mod Resources (Copper, Tin, Silver, Lead, Aluminum, Nickel, Platinum, Uranium, Zinc)
- Mekanism (Osmium)
- Big/Extreme Reactors (Uranium)

Don't see an ore or stone you want included? There's a config section for you! You can add any block you want and Geolosys will handle generating it in the same manner it generates its own ores! You can even specify a Cluster block for the surface if you want!

## Compiling

Geolosys has no crazy compilation process; all you have to do is download the repository as a zip (or clone it, your choice) and run either:

Windows Systems:
`.\gradlew.bat build`

macOS / Linux / Unix Systems:
`./gradle build`

Built files are found in `builds/libs` within the repository directory.

## Pull Requests

Are welcome! Just please document what it is you have changed via your PR Comment and / or commit messages. I would rather not dig through your code to figure out what has been changed :)

## Issues

Issues should follow the issues template provided. If they don't, don't be surprised if the issue is marked invalid.

For issues regarding Geolosys world generation, include both `geolosys_ores.json` as well as `geolosys.cfg`. It's impossible to figure out what the exact case is for world generation if I'm not sure what variables have been changed.
