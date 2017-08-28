# TFTechness
For all those unfortunate enough to stumble across this terribleness:

Get out.

Go.

Before it's too late, just go.


On your own head be it.

This is a (terrible) attempt to string together [TerraFirmaCraft](https://github.com/Deadrik/TFCraft) and various tech mods (namely [team CoFH](https://github.com/CoFH) mods and [Railcraft](https://github.com/Railcraft/Railcraft) so that they play together all nice-nice, and CoFH acts a bit more TFCraft-y.

Although not done yet, the plan is to balance the mods so that (stock) TFCraft acts as the early-mid game, and CoFH (and potentially other tech mods) as the late game.

The ultimate goal (which will probably never be reached) is to allow for a TerraFirmaCraft game that starts out being survival-y, manual, tedious, and all the things good ol' TFCraft is, and eventually lead to full automation of everything with the four of us sitting around a table talking about how much happier we were back in the TFCraft days; "Who'd a thought thirty years ago we'd all be sittin' here drinking Chateau de Chassilier?"

Ideally it'll eventually move away from its reliance on ThermalExpansion (and other team CoFH mods ... and non-team CoFH mods for that matter) and become its own independent mod (this is very unlikely to happen ever)

Features so far:
* Added (heatable) TFC metals (ingots, sheets, double ingots, double sheets, unshaped metal) for all ThermalFoundation metals.
* Removed ThermalFoundation gear crafting recipes
* Added anvil recipes for gears: double metal sheet + wrought iron ingot in an anvil = gear.
* The required anvil tier will be the highest out of wrought iron and the tier of the metal sheet
* Removed ThermalExpansion portable tank recipes
* Portable tanks are crafted in several stages: 
* Working a copper sheet will produce an unfinished portable tank frame (note: the tank plan now requires the portable tanks tech)
* Welding unfinished portable tank frame with a lead ingot makes a portable tank frame
* portable tank frame surrounded with glass in the crafting table creates a portable tank
* Portable tanks can be upgraded with an appropriate double sheet (or hardened glass block) crafted with previous tier tank
* This works with welding frames & unfinished frames
* Replaced the igneous extruder with a version that accepts fresh water/salt water and TFC lava
* Replaced the aqueous accumulator with a version that can generate both fresh and salt water
* Added steel buckets for every TF bucket fluid
* Hacked water such that any mod that tries to use water will instead use TFC's fresh water
* [Early WIP] Used [TechResearch](https://github.com/Dinglydell/TechResearch) such that some recipes (or anvil plans) will require you to research tech before being available

Mods in use (and relies upon):
*[TerraFirmaCraft](https://github.com/Deadrik/TFCraft) (obviously)
*[ThermalExpansion](https://github.com/CoFH/ThermalExpansion), [ThermalFoundation](https://github.com/CoFH/ThermalFoundation), [ThermalDynamics](https://github.com/CoFH/ThermalDynamics)
*[Railcraft](https://github.com/Railcraft/Railcraft)
*[BigReactors](https://github.com/erogenousbeef/BigReactors)
*[TechResearch](https://github.com/Dinglydell/TechResearch) (I made dis)