package jsettlers.algorithms;

import jsettlers.TestWindow;
import jsettlers.common.Color;
import jsettlers.common.CommonConstants;
import jsettlers.common.landscape.ELandscapeType;
import jsettlers.common.map.IGraphicsBackgroundListener;
import jsettlers.common.map.IGraphicsGrid;
import jsettlers.common.map.partition.IPartitionSettings;
import jsettlers.common.mapobject.IMapObject;
import jsettlers.common.movable.IMovable;
import jsettlers.common.position.ShortPoint2D;
import jsettlers.graphics.action.Action;
import jsettlers.graphics.action.EActionType;
import jsettlers.graphics.action.PointAction;
import jsettlers.graphics.map.IMapInterfaceListener;
import jsettlers.graphics.map.MapInterfaceConnector;
import jsettlers.logic.algorithms.interfaces.IContainingProvider;
import jsettlers.logic.algorithms.landmarks.EnclosedBlockedAreaFinderAlgorithm;
import jsettlers.logic.algorithms.landmarks.IEnclosedBlockedAreaFinderGrid;

public class LandmarksThreadTester {
	protected static final int WIDTH = 20;
	protected static final int HEIGHT = 20;
	private static Map map;

	public static void main(String args[]) {
		map = new Map();

		MapInterfaceConnector connector = TestWindow.openTestWindow(map);
		connector.addListener(new IMapInterfaceListener() {

			@Override
			public void action(Action action) {
				if (action.getActionType() == EActionType.SELECT_POINT) {
					System.out.println("clicked: " + ((PointAction) action).getPosition());
				}
			}
		});

		test1();
		test2();
	}

	private static void test2() {
		map.setBlocked(8, 11, true);
		map.setBlocked(8, 13, true);

		setPartition(7, 11, 1);
		setPartition(7, 10, 1);
		setPartition(8, 10, 1);
		setPartition(9, 11, 1);
		setPartition(9, 12, 1);
		setPartition(8, 12, 1);

		setPartition(7, 12, 1);
	}

	private static void test1() {
		for (short x = 3; x < 6; x++) {
			for (short y = 5; y < 7; y++) {
				map.setBlocked(x, y, true);
			}
		}

		setPartition(2, 4, 1);
		setPartition(2, 5, 1);
		setPartition(2, 6, 1);

		setPartition(6, 5, 1);
		setPartition(6, 6, 1);
		setPartition(6, 7, 1);

		setPartition(3, 4, 1);
		setPartition(4, 4, 1);
		setPartition(5, 4, 1);

		setPartition(3, 7, 1);
		setPartition(4, 7, 1);
		setPartition(5, 7, 1);
	}

	private static void setPartition(int x, int y, int partition) {
		map.setPartitionAt((short) x, (short) y, (short) partition);
		ShortPoint2D pos = new ShortPoint2D(x, y);
		EnclosedBlockedAreaFinderAlgorithm.checkLandmark(map, new IContainingProvider() {
			@Override
			public boolean contains(int x, int y) {
				return map.blocked[x][y];
			}
		}, pos);
	}

	// private static void printMap(Map map) {
	// for (short y = HEIGHT - 1; y >= 0; y--) {
	// printSpaces(y * 10);
	// for (short x = 0; x < WIDTH; x++) {
	// System.out.print("      (" + x + "|" + y + ")");
	// if (map.isBlocked(x, y)) {
	// System.out.print("b");
	// } else {
	// System.out.print(" ");
	// }
	// System.out.print("|" + map.getPartitionAt(x, y) + "      ");
	// }
	// System.out.println();
	// }
	// }

	// private static void printSpaces(int spaces) {
	// for (int i = 0; i < spaces; i++) {
	// System.out.print(" ");
	// }
	// }

	private static class Map implements IEnclosedBlockedAreaFinderGrid, IGraphicsGrid {
		short[][] partitions = new short[WIDTH][HEIGHT];
		boolean[][] blocked = new boolean[WIDTH][HEIGHT];

		@Override
		public void setPartitionAt(int x, int y, short partition) {
			this.partitions[x][y] = partition;
		}

		@Override
		public boolean isInBounds(int x, int y) {
			return 0 <= x && x < WIDTH && 0 <= y && y < HEIGHT;
		}

		@Override
		public boolean isBlocked(int x, int y) {
			return blocked[x][y];
		}

		@Override
		public short getPartitionAt(int x, int y) {
			return partitions[x][y];
		}

		void setBlocked(int x, int y, boolean blocked) {
			this.blocked[x][y] = blocked;
		}

		@Override
		public short getHeight() {
			return HEIGHT;
		}

		@Override
		public short getWidth() {
			return WIDTH;
		}

		@Override
		public IMovable getMovableAt(int x, int y) {
			return null;
		}

		@Override
		public IMapObject getMapObjectsAt(int x, int y) {
			return null;
		}

		@Override
		public byte getHeightAt(int x, int y) {
			return 0;
		}

		@Override
		public ELandscapeType getLandscapeTypeAt(int x, int y) {
			return ELandscapeType.GRASS;
		}

		@Override
		public int getDebugColorAt(int x, int y) {
			return Color.getARGB(isBlocked((short) x, (short) y) ? 1 : 0, 0, getPartitionAt((short) x, (short) y) / 2f, 1);
		}

		@Override
		public boolean isBorder(int x, int y) {
			return false;
		}

		@Override
		public int nextDrawableX(int x, int y, int maxX) {
			return x + 1;
		}

		@Override
		public byte getPlayerIdAt(int x, int y) {
			return 0;
		}

		@Override
		public byte getVisibleStatus(int x, int y) {
			return CommonConstants.FOG_OF_WAR_VISIBLE;
		}

		@Override
		public boolean isFogOfWarVisible(int x, int y) {
			return true;
		}

		@Override
		public void setBackgroundListener(IGraphicsBackgroundListener backgroundListener) {
		}

		@Override
		public IPartitionSettings getPartitionSettings(int x, int y) {
			return null;
		}

	}
}
