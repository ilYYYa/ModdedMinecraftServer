package ru.ilyyya.serverTestModification;

public class ilStructures
{
	//For CreepiSquidy event
	public static String[][] arena =
		{

				{
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
				},
				{
					"11111111111111111",
					"11111111111111111",
					"11100001110000111",
					"11000000000000011",
					"11004000000040011",
					"11000100000100011",
					"11000031113000011",
					"11100011111000111",
					"11100011111000111",
					"11100011111000111",
					"11000031113000011",
					"11000100000100011",
					"11004000000040011",
					"11000000000000011",
					"11100001110000111",
					"11111111111111111",
					"11111111111111111",
				},
				{
					"11111111111111111",
					"12111111511111121",
					"11100000000000111",
					"11000000000000011",
					"11000000000000011",
					"11000100000100011",
					"11000000000000011",
					"11000000000000011",
					"15000000300000051",
					"11000000000000011",
					"11000000000000011",
					"11000100000100011",
					"11000000000000011",
					"11000000000000011",
					"11100000000000111",
					"12111111511111121",
					"11111111111111111",
				},
				{
					"11111111111111111",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000100000100001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000100000100001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"11111111111111111",
				},
				{
					"11111111111111111",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000100000100001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000100000100001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"11111111111111111",
				},
				{
					"11111111111111111",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000100000100001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000100000100001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"10000000000000001",
					"11111111111111111",
				},
				{
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
					"11111111111111111",
				}
		};

	/**
	 * 0 - ANYTHING
	 * 1 - ANY FULLEST BLOCK
	 * 2 - 
	 * 3 - DOOR
	 * 4 - 
	 * 5 - STAIRS
	 * 6 - GLASS PANEL
	 * 7 - 
	 * 8 - 
	 */
	public static final int[][][] VillageHouseT0 =
	{
		{
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1},
			{0,0,5,0}
		},
		{
			{1,1,1,1},
			{1,0,0,1},
			{1,0,0,1},
			{1,0,0,1},
			{1,1,3,1},
			{0,0,0,0}
		},
		{
			{1,1,1,1},
			{1,0,0,1},
			{6,0,0,6},
			{1,0,0,1},
			{1,1,0,1},
			{0,0,0,0},
		},
		{
			{1,1,1,1},
			{1,0,0,1},
			{1,0,0,1},
			{1,0,0,1},
			{1,1,1,1},
			{0,0,0,0}
		},
		{
			{0,1,1,0},
			{1,1,1,1},
			{1,1,1,1},
			{1,1,1,1},
			{0,1,1,0},
			{0,0,0,0}
		}
	};
	public static final int[][][] VillageHouseT1 =
		{
			{
				{1,1,1,1},
				{1,1,1,1},
				{1,1,1,1},
				{1,1,1,1},
				{1,1,1,1},
				{0,0,5,0}
			},
			{
				{1,1,1,1},
				{1,0,0,1},
				{1,0,0,1},
				{1,0,0,1},
				{1,1,3,1},
				{0,0,0,0}
			},
			{
				{1,1,1,1},
				{1,0,0,1},
				{6,0,0,6},
				{1,0,0,1},
				{1,1,0,1},
				{0,0,0,0},
			},
			{
				{1,1,1,1},
				{1,0,0,1},
				{1,0,0,1},
				{1,0,0,1},
				{1,1,1,1},
				{0,0,0,0}
			},
			{
				{0,1,1,0},
				{1,0,0,1},
				{1,0,0,1},
				{1,0,0,1},
				{0,1,1,0},
				{0,0,0,0}
			},
			{
				{0,0,0,0},
				{0,1,1,0},
				{0,1,1,0},
				{0,1,1,0},
				{0,0,0,0},
				{0,0,0,0}
			}
		};
	public static final int[][][] VillageHouseT2 =
		{
			{
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{0,0,5,0,0},
			},
			{
				{1,1,1,1,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,1,3,1,1},
				{0,0,0,0,0},
			},
			{
				{1,1,6,1,1},
				{1,0,0,0,1},
				{6,0,0,0,6},
				{1,0,0,0,1},
				{1,1,0,1,1},
				{0,0,0,0,0},
			},
			{
				{1,1,1,1,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,1,1,1,1},
				{0,0,0,0,0},
			},
			{
				{1,1,1,1,1},
				{1,0,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{0,0,0,0,0},
			}
		};
	public static final int[][][] VillageHouseT3 =
		{
			{
				{0,0,1,1,1,1,1,1,1},
				{0,0,1,1,1,1,1,1,1},
				{0,0,1,1,1,1,1,1,1},
				{0,0,1,1,1,1,1,1,1},
				{0,0,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{0,0,5,0,0,0,0,0,0},
			},
			{
				{0,0,1,1,1,1,1,1,1},
				{0,0,1,0,0,0,0,0,1},
				{0,0,1,0,0,0,0,0,1},
				{0,0,1,0,0,0,0,0,1},
				{0,0,1,0,0,0,0,0,1},
				{1,1,1,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,1,3,1,1,1,1,1,1},
				{0,0,0,0,0,0,0,0,0},
			},
			{
				{0,0,1,1,1,1,1,1,1},
				{0,0,1,0,0,0,0,0,1},
				{0,0,6,0,0,0,0,0,6},
				{0,0,6,0,0,0,0,0,6},
				{0,0,1,0,0,0,0,0,1},
				{1,1,1,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{6,0,0,0,0,0,0,0,6},
				{6,0,0,0,0,0,0,0,6},
				{1,0,0,0,0,0,0,0,1},
				{1,1,0,1,1,6,1,1,1},
				{0,0,0,0,0,0,0,0,0},
			},
			{
				{0,5,1,1,1,1,1,1,1},
				{0,5,1,0,0,0,0,0,1},
				{0,5,1,0,0,0,0,0,1},
				{0,5,1,0,0,0,0,0,1},
				{5,1,1,0,0,0,0,0,1},
				{1,1,1,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,1,1,1,1,1,1,1,1},
				{5,5,5,5,5,5,5,5,5},
			},
			{
				{0,0,5,1,1,6,1,1,5},
				{0,0,5,1,0,0,0,1,5},
				{0,0,5,1,0,0,0,1,5},
				{0,0,5,1,0,0,0,1,5},
				{0,0,5,1,0,0,0,1,5},
				{5,5,1,1,0,0,0,1,5},
				{1,1,1,1,0,0,0,1,1},
				{1,0,0,0,0,0,0,1,1},
				{1,0,0,0,0,0,0,1,1},
				{1,1,1,1,1,1,1,1,1},
				{5,5,5,5,5,5,5,5,5},
				{0,0,0,0,0,0,0,0,0},
			},
			{
				{0,0,0,5,1,1,1,5,0},
				{0,0,0,5,1,0,1,5,0},
				{0,0,0,5,1,0,1,5,0},
				{0,0,0,5,1,0,1,5,0},
				{0,0,0,5,1,0,1,5,0},
				{0,0,0,5,1,0,1,5,0},
				{5,5,5,1,1,0,1,1,5},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{5,5,5,5,5,5,5,5,5},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
			},
			{
				{0,0,0,0,5,1,5,0,0},
				{0,0,0,0,5,1,5,0,0},
				{0,0,0,0,5,1,5,0,0},
				{0,0,0,0,5,1,5,0,0},
				{0,0,0,0,5,1,5,0,0},
				{0,0,0,0,5,1,5,0,0},
				{0,0,0,0,5,1,5,0,0},
				{5,5,5,5,1,1,1,5,5},
				{5,5,5,5,5,5,5,5,5},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
			}
		};
	public static final int[][][] VillageHouseT4 =
		{
			{
				{0,0,0,0,0,0,5,0,0},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,0,0},
				{1,1,1,1,1,1,1,0,0},
				{1,1,1,1,1,1,1,0,0},
				{1,1,1,1,1,1,1,0,0},
				{1,1,1,1,1,1,1,0,0}
			},
			{
				{0,0,0,0,0,0,0,0,0},
				{1,1,1,1,1,1,3,1,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,1,3,1,1,1,1,1,1},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}
			},
			{
				{0,0,0,0,0,0,0,0,0},
				{1,1,1,6,1,1,0,1,1},
				{1,0,0,0,0,0,0,0,1},
				{6,0,0,0,0,0,0,0,6},
				{6,0,0,0,0,0,0,0,6},
				{1,0,0,0,0,0,0,0,1},
				{1,1,0,1,1,6,1,1,1},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}
			},
			{
				{5,5,5,5,5,5,5,5,5},
				{1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,1,1,1,1,1,1,1,1},
				{5,5,5,5,5,5,5,5,5},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}
			},
			{
				{0,0,0,0,0,0,0,0,0},
				{5,5,5,5,5,5,5,5,5},
				{1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,0,0,1},
				{1,0,0,0,0,0,0,0,1},
				{1,1,1,1,1,1,1,1,1},
				{5,5,5,5,5,5,5,5,5},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}
			},
			{
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{5,5,5,5,5,5,5,5,5},
				{1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1},
				{5,5,5,5,5,5,5,5,5},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}
			},
			{
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{5,5,5,5,5,5,5,5,5},
				{5,5,5,5,5,5,5,5,5},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}
			}
		};
	public static final int[][][] VillageHouseT5 =
		{
			{
				{0,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,0},
				{5,1,1,1,1,1,1,0},
				{0,1,1,1,1,1,1,0}
			},
			{
				{0,1,1,1,1,1,1,0},
				{0,1,0,0,0,0,1,0},
				{0,1,0,0,0,0,1,0},
				{0,1,0,0,0,0,1,0},
				{0,1,0,0,0,0,1,0},
				{0,1,0,0,0,0,1,0},
				{0,1,0,0,0,0,1,0},
				{0,3,0,0,0,0,1,0},
				{0,1,1,1,1,1,1,0}
			},
			{
				{0,1,1,6,6,1,1,0},
				{0,1,0,0,0,0,1,0},
				{0,6,0,0,0,0,6,0},
				{0,6,0,0,0,0,6,0},
				{0,6,0,0,0,0,1,0},
				{0,1,0,0,0,0,6,0},
				{0,1,0,0,0,0,6,0},
				{0,0,0,0,0,0,1,0},
				{0,1,1,6,6,1,1,0}
			},
			{
				{0,1,1,6,6,1,1,0},
				{0,1,0,0,0,0,1,0},
				{0,6,0,0,0,0,1,0},
				{0,6,0,0,0,0,1,0},
				{0,6,0,0,0,0,1,0},
				{0,1,0,0,0,0,1,0},
				{0,1,0,0,0,0,1,0},
				{0,1,0,0,0,0,1,0},
				{0,1,1,6,6,1,1,0}
			},
			{
				{0,1,1,1,1,1,1,0},
				{0,1,1,0,0,1,1,0},
				{0,1,1,0,0,1,1,0},
				{0,1,1,0,0,1,1,0},
				{0,1,1,0,0,1,1,0},
				{0,1,1,0,0,1,1,0},
				{0,1,1,0,0,1,1,0},
				{0,1,1,0,0,1,1,0},
				{0,1,1,1,1,1,1,0}
			},
			{
				{5,1,1,1,1,1,1,5},
				{5,1,1,1,1,1,1,5},
				{5,1,1,1,1,1,1,5},
				{5,1,1,1,1,1,1,5},
				{5,1,1,1,1,1,1,5},
				{5,1,1,1,1,1,1,5},
				{5,1,1,1,1,1,1,5},
				{5,1,1,1,1,1,1,5},
				{5,1,1,1,1,1,1,5}
			},
			{
				{0,5,1,1,1,1,5,0},
				{0,5,1,1,1,1,5,0},
				{0,5,1,1,1,1,5,0},
				{0,5,1,1,1,1,5,0},
				{0,5,1,1,1,1,5,0},
				{0,5,1,1,1,1,5,0},
				{0,5,1,1,1,1,5,0},
				{0,5,1,1,1,1,5,0},
				{0,5,1,1,1,1,5,0}
			},
			{
				{0,0,5,1,1,5,0,0},
				{0,0,5,1,1,5,0,0},
				{0,0,5,1,1,5,0,0},
				{0,0,5,1,1,5,0,0},
				{0,0,5,1,1,5,0,0},
				{0,0,5,1,1,5,0,0},
				{0,0,5,1,1,5,0,0},
				{0,0,5,1,1,5,0,0},
				{0,0,5,1,1,5,0,0}
			},
			{
				{0,0,0,5,5,0,0,0},
				{0,0,0,5,5,0,0,0},
				{0,0,0,5,5,0,0,0},
				{0,0,0,5,5,0,0,0},
				{0,0,0,5,5,0,0,0},
				{0,0,0,5,5,0,0,0},
				{0,0,0,5,5,0,0,0},
				{0,0,0,5,5,0,0,0},
				{0,0,0,5,5,0,0,0}
			}
		};
	public static final int[][][] VillageHouseT6 =
		{

			{
				{0,0,5,0,0},
				{0,1,1,1,0},
				{0,1,1,1,0},
				{0,1,1,1,0},
				{0,1,1,1,0},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{0,1,1,1,0}
			},
			{
				{0,0,0,0,0},
				{0,1,3,1,0},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,5,0,5,1},
				{1,1,5,1,1},
				{1,1,1,1,1},
				{0,1,1,1,0}
			},
			{
				{0,0,0,0,0},
				{0,1,0,1,0},
				{1,0,0,0,1},
				{6,0,0,0,6},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,5,0,5,1},
				{0,1,1,1,0}
			},
			{
				{0,0,0,0,0},
				{0,1,1,1,0},
				{1,0,0,0,1},
				{6,0,0,0,6},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{6,0,0,0,6},
				{1,0,0,0,1},
				{0,1,6,1,0}
			},
			{
				{0,0,0,0,0},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,0,1,1,1},
				{1,1,1,1,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{0,1,1,1,0}
			},
			{
				{0,0,0,0,0},
				{0,1,1,1,0},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{0,1,1,1,0},
				{0,1,1,1,0},
				{0,1,1,1,0},
				{0,1,1,1,0},
				{0,0,0,0,0}
			},
			{
				{0,0,0,0,0},
				{0,1,6,1,0},
				{1,0,0,0,1},
				{6,0,0,0,6},
				{1,0,0,0,1},
				{0,1,6,1,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,0,0,0,0},
				{0,1,6,1,0},
				{1,0,0,0,1},
				{6,0,0,0,6},
				{1,0,0,0,1},
				{0,1,6,1,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,0,0,0,0},
				{0,1,1,1,0},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{0,1,1,1,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,0,0,0,0},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,1,1,1,1},
				{1,0,1,1,1},
				{1,1,1,1,1},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,0,0,0,0},
				{0,1,1,1,0},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{1,0,0,0,1},
				{0,1,1,1,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,0,0,0,0},
				{0,0,1,0,0},
				{0,0,0,0,0},
				{1,0,0,0,1},
				{0,0,0,0,0},
				{0,0,1,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			}
		};
	public static final int[][][][] Houses =
		{
				VillageHouseT0, VillageHouseT1, VillageHouseT2, VillageHouseT3, VillageHouseT4, VillageHouseT5, VillageHouseT6
		};
}
































