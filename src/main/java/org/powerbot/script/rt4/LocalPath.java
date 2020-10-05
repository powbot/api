package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.Client;
import org.powerbot.bot.rt4.client.*;
import org.powerbot.script.Tile;
import org.powerbot.script.*;

import java.util.LinkedList;
import java.util.*;

/**
 * LocalPath
 */
public class LocalPath extends Path {

	private final Locatable destination;
	private TilePath tilePath;
	private Tile tile;

	LocalPath(final ClientContext ctx, final Locatable destination) {
		super(ctx);
		this.destination = destination;
	}

	public TilePath getTilePath() {
		return this.tilePath;
	}

	static Graph getGraph(final ClientContext ctx) {
		final Client client = ctx.client();
		if (client == null) {
			return null;
		}
		final int floor = client.getFloor();
		final CollisionMap[] maps = client.getCollisionMaps();
		final CollisionMap map;
		if (maps == null || floor < 0 || floor >= maps.length || (map = maps[floor]) == null) {
			return null;
		}
		final int[][] arr = map.getFlags();
		return arr != null ? new Graph(arr, map.getOffsetX(), map.getOffsetY()) : null;
	}

	static Node bfs(final Graph graph, final Node source, final Node target) {
		Queue<Node> queue = new ArrayDeque<>();
		queue.offer(source);
		source.g = 0;

		while (!queue.isEmpty()) {
			Node current = queue.poll();
			if (current.equals(target)) {
				return current;
			}
			List<Node> neighbors = graph.neighbors(current);
			for (Node neighbor : neighbors) {
				if (neighbor.g == Integer.MAX_VALUE) {
					neighbor.g = current.g + 1;
					neighbor.parent = current;
					queue.offer(neighbor);
				}
			}
		}
		return null;
	}

	static Node[] follow(Node target) {
		final List<Node> nodes = new LinkedList<>();
		if (target.g == Integer.MAX_VALUE) {
			return new Node[0];
		}
		while (target != null) {
			nodes.add(target);
			target = target.parent;
		}

		Collections.reverse(nodes);
		final Node[] path = new Node[nodes.size()];
		return nodes.toArray(path);
	}

	@Override
	public boolean traverse(final EnumSet<TraversalOption> options) {
		return valid() && tilePath.traverse(options);
	}

	@Override
	public Tile next() {
		return valid() ? tilePath.next() : Tile.NIL;
	}

	@Override
	public Tile start() {
		return ctx.players.local().tile();
	}

	@Override
	public Tile end() {
		return destination.tile();
	}

	@Override
	public boolean valid() {
		Tile end = destination.tile();
		if (end == null || end == Tile.NIL) {
			return false;
		}
		if (end.equals(tile) && tilePath != null) {
			return true;
		}
		tile = end;
		Tile start = ctx.players.local().tile();
		final Tile base = ctx.game.mapOffset();
		if (base == Tile.NIL || start == Tile.NIL) {
			return false;
		}
		start = start.derive(-base.x(), -base.y());
		end = end.derive(-base.x(), -base.y());

		final Graph graph = getGraph(ctx);
		final Node[] path;
		final Node nodeStart, nodeStop;
		if (graph != null &&
			(nodeStart = graph.getNode(start.x(), start.y())) != null &&
			(nodeStop = graph.getNode(end.x(), end.y())) != null) {
			bfs(graph, nodeStart, nodeStop);
			path = follow(nodeStop);
		} else {
			path = new Node[0];
		}
		if (path.length > 0) {
			final Tile[] arr = new Tile[path.length];
			for (int i = 0; i < path.length; i++) {
				arr[i] = base.derive(path[i].x, path[i].y);
			}
			tilePath = new TilePath(ctx, arr);
			return true;
		}
		return false;
	}

	static final class Graph {

		private final int offX, offY;
		private final Node[][] nodes;
		private final int width, height;
		private final int[][] flags;

		private Graph(final int[][] flags, final int offX, final int offY) {
			this.flags = flags;
			this.offX = offX;
			this.offY = offY;
			nodes = new Node[flags.length][];
			width = flags.length;
			int height = flags.length;
			for (int x = 0; x < flags.length; x++) {
				final int[] arr = flags[x];
				nodes[x] = new Node[arr.length];
				height = Math.min(height, arr.length);
			}
			this.height = height;
		}

		Node getNode(final int x, final int y) {
			final int ox = x - offX, oy = y - offY;
			if (ox < 0 || oy < 0 || ox >= nodes.length || oy >= nodes[ox].length) {
				return new Node(-1, -1, BLOCKED);
			}
			// Lazily initialize graph
			final Node node = nodes[ox][oy];
			if (node == null) {
				return nodes[ox][oy] = new Node(x, y, flags[ox][oy]);
			}
			return node;
		}

		private List<Node> neighbors(final Node node) {
			final List<Node> list = new ArrayList<>(8);
			final int curr_x = node.x;
			final int curr_y = node.y;
			if (curr_x < 0 || curr_y < 0 ||
				curr_x >= width || curr_y >= height) {
				return list;
			}
			if (curr_y > 0 &&
				(getNode(curr_x, curr_y).flag & WALL_SOUTH) == 0 &&
				(getNode(curr_x, curr_y - 1).flag & BLOCKED) == 0) {
				list.add(getNode(curr_x, curr_y - 1));
			}
			if (curr_x > 0 &&
				(getNode(curr_x, curr_y).flag & WALL_WEST) == 0 &&
				(getNode(curr_x - 1, curr_y).flag & BLOCKED) == 0) {
				list.add(getNode(curr_x - 1, curr_y));
			}
			if (curr_y < height - 1 &&
				(getNode(curr_x, curr_y).flag & WALL_NORTH) == 0 &&
				(getNode(curr_x, curr_y + 1).flag & BLOCKED) == 0) {
				list.add(getNode(curr_x, curr_y + 1));
			}
			if (curr_x < width - 1 &&
				(getNode(curr_x, curr_y).flag & WALL_EAST) == 0 &&
				(getNode(curr_x + 1, curr_y).flag & BLOCKED) == 0) {
				list.add(getNode(curr_x + 1, curr_y));
			}
			if (curr_x > 0 && curr_y > 0 &&
				(getNode(curr_x, curr_y).flag & (WALL_SOUTHWEST | WALL_SOUTH | WALL_WEST)) == 0 &&
				(getNode(curr_x - 1, curr_y - 1).flag & BLOCKED) == 0 &&
				(getNode(curr_x, curr_y - 1).flag & (WALL_WEST | BLOCKED)) == 0 &&
				(getNode(curr_x - 1, curr_y).flag & (WALL_SOUTH | BLOCKED)) == 0) {
				list.add(getNode(curr_x - 1, curr_y - 1));
			}
			if (curr_x > 0 && curr_y < height - 1 &&
				(getNode(curr_x, curr_y).flag & (WALL_NORTHWEST | WALL_NORTH | WALL_WEST)) == 0 &&
				(getNode(curr_x - 1, curr_y + 1).flag & BLOCKED) == 0 &&
				(getNode(curr_x, curr_y + 1).flag & (WALL_WEST | BLOCKED)) == 0 &&
				(getNode(curr_x - 1, curr_y).flag & (WALL_NORTH | BLOCKED)) == 0) {
				list.add(getNode(curr_x - 1, curr_y + 1));
			}
			if (curr_x < height - 1 && curr_y > 0 &&
				(getNode(curr_x, curr_y).flag & (WALL_SOUTHEAST | WALL_SOUTH | WALL_EAST)) == 0 &&
				(getNode(curr_x + 1, curr_y - 1).flag & BLOCKED) == 0 &&
				(getNode(curr_x, curr_y - 1).flag & (WALL_EAST | BLOCKED)) == 0 &&
				(getNode(curr_x + 1, curr_y).flag & (WALL_SOUTH | BLOCKED)) == 0) {
				list.add(getNode(curr_x + 1, curr_y - 1));
			}
			if (curr_x < width - 1 && curr_y < height - 1 &&
				(getNode(curr_x, curr_y).flag & (WALL_NORTHEAST | WALL_NORTH | WALL_EAST)) == 0 &&
				(getNode(curr_x + 1, curr_y + 1).flag & BLOCKED) == 0 &&
				(getNode(curr_x, curr_y + 1).flag & (WALL_EAST | BLOCKED)) == 0 &&
				(getNode(curr_x + 1, curr_y).flag & (WALL_NORTH | BLOCKED)) == 0) {
				list.add(getNode(curr_x + 1, curr_y + 1));
			}
			return list;
		}
	}

	static final class Node {

		public final int x, y;
		public final int flag;
		private Node parent = null;
		private int g = Integer.MAX_VALUE;

		private Node(final int x, final int y, final int flag) {
			this.x = x;
			this.y = y;
			this.flag = flag;
		}

		@Override
		public String toString() {
			return Node.class.getSimpleName() + "[x=" + x + ",y=" + y + "]";
		}

		@Override
		public boolean equals(final Object o) {
			if (!(o instanceof Node)) {
				return false;
			}
			final Node n = (Node) o;
			return x == n.x && y == n.y;
		}
	}
}
