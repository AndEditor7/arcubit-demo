package com.andedit.arcubit.block;

import static com.andedit.arcubit.renderer.TexLib.getTex;

public final class Blocks
{		
	private static byte i = 0;
	public static final byte 
	AIR = 			i++,
	STONE = 		i++,
	SNOWSTONE = 	i++,
	COBSTONE = 		i++,
	DIRT = 			i++,
	GRASS = 		i++,
	SNOWGRASS = 	i++,
	SANDSTONE = 	i++,
	SAND = 			i++,
	GRAVEL = 		i++,
	CLAY = 			i++,
	CACTUS = 		i++,
	GLASS = 		i++,
	COALORE = 		i++,
	COPPERORE = 	i++,
	GOLDORE = 		i++,
	WATER = 		i++,
	LAVA = 			i++,
	LEAVES = 		i++,
//	DARKLEAVES = 	i++,
	LOG = 			i++,
//	DARKLOG = 		i++,
	DARKWOOD = 		i++,
	WOOD = 			i++,
	LIGHTWOOD = 	i++,
	STONEBRICK = 	i++,
	BRICK = 		i++,
	STONEMOSS = 	i++,
	GOLD = 			i++,
	COPPER = 		i++,
	METAL = 		i++,
	TALLGRASS = 	i++,
	FLOWER = 		i++,
	ROSE = 			i++,
	SAPLING = 		i++,
	SHRUB = 		i++,
	HELLROCK = 		i++,
	ICE = 			i++,
	SNOW = 			i++,
	OBSIDIAN = 		i++,
	TNT = 			i++,
	BEDROCK = 		i++,
	WOOL = 			i++,
	WOOLRED = 		i++,
	WOOLORANGE = 	i++,
	WOOLYELLOW = 	i++,
	WOOLLIME = 		i++,
	WOOLGREEN = 	i++,
	WOOLCYAN = 		i++,
	WOOLBLUEL = 	i++,
	WOOLBLUE = 		i++,
	WOOLPURPLE = 	i++,
	WOOLPINK = 		i++,
	WOOLMAG = 		i++,
	WOOLGRAYL = 	i++,
	WOOLGRAY = 		i++,
	WOOLBLACK = 	i++,
	WOOLBROWN = 	i++;
	
	public static final int size = i;
	public static final Block[] blocks = new Block[size];
	
	/** Make sure the textures loaded first. */
	public static void loadBlocks() {
		blocks[AIR] = new Block(AIR, "Air", false, BlockType.AIR);
		blocks[STONE] = new Block(STONE, "Stone", true, BlockType.STONE);
		blocks[SNOWSTONE] = new Block(SNOWSTONE, "Snow Stone", true, BlockType.STONE);
		blocks[COBSTONE] = new Block(COBSTONE, "Cobblestone", true, BlockType.STONE);
		blocks[DIRT] = new Block(DIRT, "Dirt", true, BlockType.SOIL);
		blocks[GRASS] = new Block(GRASS, "Grass", true, BlockType.SOIL);
		blocks[SNOWGRASS] = new Block(SNOWGRASS, "Snow Grass", true, BlockType.SOIL);
		blocks[SANDSTONE] = new Block(SANDSTONE, "Sandstone", true, BlockType.STONE);
		blocks[SAND] = new Block(SAND, "Sand", true, BlockType.SAND);
		blocks[GRAVEL] = new Block(GRAVEL, "Gravel", true, BlockType.GRAVEL);
		blocks[CLAY] = new Block(CLAY, "Clay", true, BlockType.SAND);
		blocks[CACTUS] = new Block(CACTUS, "Cactus", true, BlockType.WOOD);
		blocks[GLASS] = new Block(GLASS, "Glass", false, true, BlockType.GLASS);
		blocks[COALORE] = new Block(COALORE, "Coal Ore", true, BlockType.STONE);
		blocks[COPPERORE] = new Block(COPPERORE, "Copper Ore", true, BlockType.STONE);
		blocks[GOLDORE] = new Block(GOLDORE, "Gold Ore", true, BlockType.STONE);
		blocks[WATER] = new Block(WATER, "Water", false, BlockType.WATER);
		blocks[LAVA] = new Block(LAVA, "Lava", false, BlockType.LAVA);
		blocks[LEAVES] = new Block(LEAVES, "Leaves", true, true, BlockType.LEAVES);
		//blocks[DARKLEAVES] = new Block(DARKLEAVES, "Dark Leaves", false, true, BlockType.LEAVES);
		blocks[LOG] = new Block(LOG, "Log", true, BlockType.WOOD);
		//blocks[DARKLOG] = new Block(DARKLOG, "Dark Log", true, BlockType.WOOD);
		blocks[DARKWOOD] = new Block(DARKWOOD, "Dark Wood", true, BlockType.WOOD);
		blocks[WOOD] = new Block(WOOD, "Wood", true, BlockType.WOOD);
		blocks[LIGHTWOOD] = new Block(LIGHTWOOD, "Light Wood", true, BlockType.WOOD);
		blocks[STONEBRICK] = new Block(STONEBRICK, "Stone Brick", true, BlockType.STONE);
		blocks[BRICK] = new Block(BRICK, "Brick", true, BlockType.STONE);
		blocks[STONEMOSS] = new Block(STONEMOSS, "Mossy Cobblestone", true, BlockType.STONE);
		blocks[GOLD] = new Block(GOLD, "Gold Block", true, BlockType.METAL);
		blocks[COPPER] = new Block(COPPER, "Copper Block", true, BlockType.METAL);
		blocks[METAL] = new Block(METAL, "Metal Block", true, BlockType.METAL);
		blocks[TALLGRASS] = new Block(TALLGRASS, "Tall Grass", false, BlockType.PLANT);
		blocks[FLOWER] = new Block(FLOWER, "Flower", false, BlockType.PLANT);
		blocks[ROSE] = new Block(ROSE, "Rose", false, BlockType.PLANT);
		blocks[SAPLING] = new Block(SAPLING, "Sapling", false, BlockType.PLANT);
		blocks[SHRUB] = new Block(SHRUB, "Shrub", false, BlockType.PLANT); // TODO: Make Srub work on sand
		blocks[HELLROCK] = new Block(HELLROCK, "Hellrock", true, BlockType.STONE);
		blocks[ICE] = new Block(ICE, "Ice Block", true, BlockType.STONE);
		blocks[SNOW] = new Block(SNOW, "Snow Block", true, BlockType.SOIL);
		blocks[OBSIDIAN] = new Block(OBSIDIAN, "Obsidian", true, BlockType.STONE);
		blocks[TNT] = new Block(TNT, "TNT", true, BlockType.WOOD);
		blocks[BEDROCK] = new Block(BEDROCK, "Bedrock", true, BlockType.STONE);
		blocks[WOOL] = new Block(WOOL, "White Wool", true, BlockType.WOOL);
		blocks[WOOLRED] = new Block(WOOLRED, "Red Wool", true, BlockType.WOOL);
		blocks[WOOLORANGE] = new Block(WOOLORANGE, "Orange Wool", true, BlockType.WOOL);
		blocks[WOOLYELLOW] = new Block(WOOLYELLOW, "Yellow Wool", true, BlockType.WOOL);
		blocks[WOOLLIME] = new Block(WOOLLIME, "Lime Wool", true, BlockType.WOOL);
		blocks[WOOLGREEN] = new Block(WOOLGREEN, "Green Wool", true, BlockType.WOOL);
		blocks[WOOLCYAN] = new Block(WOOLCYAN, "Cyan Wool", true, BlockType.WOOL);
		blocks[WOOLBLUEL] = new Block(WOOLBLUEL, "Light Blue Wool", true, BlockType.WOOL);
		blocks[WOOLBLUE] = new Block(WOOLBLUE, "Blue Wool", true, BlockType.WOOL);
		blocks[WOOLPURPLE] = new Block(WOOLPURPLE, "Purple Wool", true, BlockType.WOOL);
		blocks[WOOLPINK] = new Block(WOOLPINK, "Pink Wool", true, BlockType.WOOL);
		blocks[WOOLMAG] = new Block(WOOLMAG, "Magenta Wool", true, BlockType.WOOL);
		blocks[WOOLGRAYL] = new Block(WOOLGRAYL, "Light Gray Wool", true, BlockType.WOOL);
		blocks[WOOLGRAY] = new Block(WOOLGRAY, "Gray Wool", true, BlockType.WOOL);
		blocks[WOOLBLACK] = new Block(WOOLBLACK, "Black Wool", true, BlockType.WOOL);
		blocks[WOOLBROWN] = new Block(WOOLBROWN, "Brown Wool", true, BlockType.WOOL);
	}
	
	public static void loadTextures() {;
		blocks[STONE].tex(getTex("stone"));
		blocks[SNOWSTONE].tex(getTex("snow"), getTex("stone_snow"), getTex("stone"));
		blocks[COBSTONE].tex(getTex("cobblestone"));
		blocks[DIRT].tex(getTex("dirt"));
		blocks[GRASS].tex(getTex("grass_top"), getTex("grass_side"), getTex("dirt"));
		blocks[SNOWGRASS].tex(getTex("snow"), getTex("grass_side_snow"), getTex("dirt"));
		blocks[SANDSTONE].tex(getTex("sandstone"));
		blocks[SAND].tex(getTex("sand"));
		blocks[GRAVEL].tex(getTex("gravel"));
		blocks[CLAY].tex(getTex("clay"));
		blocks[CACTUS].tex(getTex("cactus_top"), getTex("cactus_side"), getTex("cactus_bottom"));
		blocks[GLASS].tex(getTex("glass"));
		blocks[COALORE].tex(getTex("coal_ore"));
		blocks[COPPERORE].tex(getTex("copper_ore"));
		blocks[GOLDORE].tex(getTex("gold_ore"));
		blocks[WATER].tex(getTex("water1")); // WATER!
		blocks[LAVA].tex(getTex("lava"));
		blocks[LEAVES].tex(getTex("leaves"));
		//blocks[DARKLEAVES].tex(getTex(null));
		blocks[LOG].tex(getTex("log_top"), getTex("log_side"));
		//blocks[DARKLOG].tex(getTex(null), getTex(null));
		blocks[DARKWOOD].tex(getTex("plank_dark"));
		blocks[WOOD].tex(getTex("plank"));
		blocks[LIGHTWOOD].tex(getTex("plank_light"));
		blocks[STONEBRICK].tex(getTex("stone_brick"));
		blocks[BRICK].tex(getTex("brick"));
		blocks[STONEMOSS].tex(getTex("mossy_cobblestone"));
		blocks[GOLD].tex(getTex("gold"));
		blocks[COPPER].tex(getTex("copper"));
		blocks[METAL].tex(getTex("metal"));
		blocks[TALLGRASS].tex(getTex("grass_tall"));
		blocks[FLOWER].tex(getTex("flower"));
		blocks[ROSE].tex(getTex("rose"));
		blocks[SAPLING].tex(getTex("sapling"));
		blocks[SHRUB].tex(getTex("srub"));
		blocks[HELLROCK].tex(getTex("hellrock"));
		blocks[ICE].tex(getTex("ice"));
		blocks[SNOW].tex(getTex("snow"));
		blocks[OBSIDIAN].tex(getTex("obsidian"));
		blocks[TNT].tex(getTex("tnt_top"), getTex("tnt"), getTex("tnt_bottom"));
		blocks[BEDROCK].tex(getTex("bedrock"));
		blocks[WOOL].tex(getTex("wool_white"));
		blocks[WOOLRED].tex(getTex("wool_red"));
		blocks[WOOLORANGE].tex(getTex("wool_orange"));
		blocks[WOOLYELLOW].tex(getTex("wool_yellow"));
		blocks[WOOLLIME].tex(getTex("wool_lime"));
		blocks[WOOLGREEN].tex(getTex("wool_green"));
		blocks[WOOLCYAN].tex(getTex("wool_cyan"));
		blocks[WOOLBLUEL].tex(getTex("wool_lightblue"));
		blocks[WOOLBLUE].tex(getTex("wool_blue"));
		blocks[WOOLPURPLE].tex(getTex("wool_purple"));
		blocks[WOOLPINK].tex(getTex("wool_pink"));
		blocks[WOOLMAG].tex(getTex("wool_magenta"));
		blocks[WOOLGRAYL].tex(getTex("wool_lightgray"));
		blocks[WOOLGRAY].tex(getTex("wool_gray"));
		blocks[WOOLBLACK].tex(getTex("wool_black"));
		blocks[WOOLBROWN].tex(getTex("wool_brown"));
	}
	
	public static boolean canAddFace(Block block, int id) {
		if (id == AIR) return true;
		final Block secondary = blocks[id];
		if (block.isSoild == secondary.isSoild)
			return false;
		if (block.isSoild == true && secondary.isSoild == false)
			return true; // primary is solid and secondary is trans.
		if (block.isSoild == false && secondary.isSoild == true)
			return false; // primary is trans and secondary is solid.
		return false;
	}
}
