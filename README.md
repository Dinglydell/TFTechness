# TFTechness
For all those unfortunate enough to stumble across this terribleness:

Get out.

Go.

Before it's too late, just go.


On your own head be it.

This is a (terrible) attempt to string together [TerraFirmaCraft](https://github.com/Deadrik/TFCraft) and [team CoFH](https://github.com/CoFH) mods (namely [ThermalFoundation](https://github.com/CoFH/ThermalFoundation)) so that they play together all nice-nice, and CoFH acts a bit more TFCraft-y.

Although not done yet, the plan is to balance the mods so that (stock) TFCraft acts as the early-mid game, and CoFH (and potentially other tech mods) as the late game.

The ultimate goal (which will probably never be reached) is to allow for a TerraFirmaCraft game that starts out being survival-y, manual, tedious, and all the things good ol' TFCraft is, and eventually lead to full automation of everything with the four of us sitting around a table talking about how much happier we were back in the TFCraft days; "Who'd a thought thirty years ago we'd all be sittin' here drinking Chateau de Chassilier?"

Features so far:
* Added (heatable) TFC metals (ingots, sheets, double ingots, double sheets, unshaped metal) for all ThermalFoundation metals.
* Removed ThermalFoundation gear crafting recipes
* Added anvil recipes for gears: double metal sheet + wrought iron ingot in an anvil = gear.
* The required anvil tier will be the highest out of wrought iron and the tier of the metal sheet
* Removed ThermalExpansion portable tank recipes
* Portable tanks are crafted in several stages: 
* Working a copper sheet will produce an unfinished portable tank frame
* Copper sheet + lead ingot = portable tank frame *(wip)*
* portable tank frame + glass = portable tank *(wip)*
* Portable tanks can be upgraded with an appropriate double sheet (or hardened glass block) + previous tier tank
* This works with frames & unfinished frames
