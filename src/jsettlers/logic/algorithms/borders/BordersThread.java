package jsettlers.logic.algorithms.borders;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import jsettlers.common.movable.EDirection;
import jsettlers.common.position.ShortPoint2D;

/**
 * This thread calculates the positions that represent the border between the areas occupied by different players.
 * 
 * @author Andreas Eberle
 * 
 */
public class BordersThread implements Runnable {

	private final IBordersThreadGrid grid;
	private boolean canceled = false;
	private final LinkedBlockingQueue<ShortPoint2D> positionsQueue = new LinkedBlockingQueue<ShortPoint2D>();
	private Thread bordersThread;

	/**
	 * This constructor creates a new instance of {@link BordersThread} and automatically launches a thread for it called "bordersThread".
	 * 
	 * @param grid
	 *            the grid on that the {@link BordersThread} will be operating
	 */
	public BordersThread(IBordersThreadGrid grid) {
		this.grid = grid;
		this.bordersThread = new Thread(this);
	}

	@Override
	public void run() {
		while (!canceled) {
			ShortPoint2D position = null;
			while (position == null && !canceled) {
				try {
					position = positionsQueue.take();
				} catch (InterruptedException e) {
				}
			}
			if (!canceled) {
				calculateForPosition(position);
			}
		}
	}

	private void calculateForPosition(ShortPoint2D position) {
		short x = position.getX();
		short y = position.getY();
		byte player = grid.getPlayer�dAt(x, y);
		boolean isBorder = false;

		if (grid.getBlockedPartition(x, y) > 0) { // the position is not a blocked landscape
			for (EDirection currDir : EDirection.values) {
				short currNeighborX = currDir.getNextTileX(x);
				short currNeighborY = currDir.getNextTileY(y);

				if (!grid.isInBounds(currNeighborX, currNeighborY)) {
					continue;
				}

				byte neighborPlayer = grid.getPlayer�dAt(currNeighborX, currNeighborY);
				boolean neighborIsBorder = false;

				if (neighborPlayer != player && grid.getBlockedPartition(currNeighborX, currNeighborY) > 0) {
					isBorder = true;
				}

				if (neighborPlayer >= 0) { // this position is occupied by a player

					for (EDirection currNeighborDir : EDirection.values) {
						short nextX = currNeighborDir.getNextTileX(currNeighborX);
						short nextY = currNeighborDir.getNextTileY(currNeighborY);

						if (grid.isInBounds(nextX, nextY) && grid.getPlayer�dAt(nextX, nextY) != neighborPlayer
								&& grid.getBlockedPartition(nextX, nextY) > 0) {
							neighborIsBorder = true;
							break;
						}
					}
				} // else the position is not occupied -> don't display a border here

				grid.setBorderAt(currNeighborX, currNeighborY, neighborIsBorder);
			}
		}

		grid.setBorderAt(x, y, isBorder && player >= 0);
	}

	public void checkPosition(ShortPoint2D position) {
		synchronized (positionsQueue) {
			this.positionsQueue.offer(position);
		}
	}

	public void checkPositions(List<ShortPoint2D> occupiedPositions) {
		synchronized (positionsQueue) {
			this.positionsQueue.addAll(occupiedPositions);
		}
	}

	public void cancel() {
		this.canceled = true;
		synchronized (positionsQueue) {
			positionsQueue.notifyAll();
		}
		bordersThread.interrupt();
	}

	public void start() {
		bordersThread.setName("bordersThread");
		bordersThread.setDaemon(true);
		bordersThread.start();
	}

}
