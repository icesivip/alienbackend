package icesi.vip.alien.alien.craftMethod.labelmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;



public class LabelMap {

	public LabelMapNode[][] map;

	public LabelMap(int[][] distribution) {
		map = new LabelMapNode[distribution.length][distribution[0].length];
		for (int i = 0; i < distribution.length; i++) {
			for (int k = 0; k < distribution[0].length; k++) {

				map[i][k] = new LabelMapNode(distribution[i][k], i, k);
			}
		}

		for (int i = 0; i < distribution.length; i++) {
			for (int j = 0; j < distribution[0].length; j++) {
				try {
					map[i][j].up = map[i - 1][j];
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
				try {
					map[i][j].right = map[i][j + 1];
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					map[i][j].down = map[i + 1][j];
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					map[i][j].left = map[i][j - 1];
				} catch (Exception e) {
					// TODO: handle exception
				}
//				if (inRange(i - 1, j, distribution)) {
//					map[i][j].up = map[i - 1][j];
//				}
//				if (inRange(i, j + 1, distribution)) {
//					map[i][j].right = map[i][j + 1];
//				}
//				if (inRange(i + 1, j, distribution)) {
//					map[i][j].down = map[i + 1][j];
//				}
//				if (inRange(i, j - 1, distribution)) {
//					map[i][j].left = map[i][j - 1];
//				}
			}
		}
	}

	public boolean checkValidity() {
		HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				set.add(map[i][j].value);
			}
		}

		for (int val : set) {
			hash.put(val, 0);
		}

		Set<String> visited = new HashSet<String>();
		LinkedList<LabelMapNode> toVisit = new LinkedList<LabelMapNode>();

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (!visited.contains(i + ":" + j)) {
					int currentArea = map[i][j].value;
					hash.replace(currentArea, hash.get(currentArea) + 1);
					visited.add(i + ":" + j);
					if (map[i][j].up != null && !visited.contains(map[i][j].up.iIndex + ":" + map[i][j].up.jIndex)
							&& map[i][j].up.value == currentArea) {
						visited.add(map[i][j].up.iIndex + ":" + map[i][j].up.jIndex);
						toVisit.add(map[i][j].up);
					}
					if (map[i][j].right != null
							&& !visited.contains(map[i][j].right.iIndex + ":" + map[i][j].right.jIndex)
							&& map[i][j].right.value == currentArea) {
						visited.add(map[i][j].right.iIndex + ":" + map[i][j].right.jIndex);
						toVisit.add(map[i][j].right);
					}
					if (map[i][j].down != null && !visited.contains(map[i][j].down.iIndex + ":" + map[i][j].down.jIndex)
							&& map[i][j].down.value == currentArea) {
						visited.add(map[i][j].down.iIndex + ":" + map[i][j].down.jIndex);
						toVisit.add(map[i][j].down);
					}
					if (map[i][j].left != null && !visited.contains(map[i][j].left.iIndex + ":" + map[i][j].left.jIndex)
							&& map[i][j].left.value == currentArea) {
						visited.add(map[i][j].left.iIndex + ":" + map[i][j].left.jIndex);
						toVisit.add(map[i][j].left);
					}

					while (!toVisit.isEmpty()) {
						LabelMapNode current = toVisit.poll();
						if (current.up != null && !visited.contains(current.up.iIndex + ":" + current.up.jIndex)
								&& current.up.value == currentArea) {
							visited.add(current.up.iIndex + ":" + current.up.jIndex);
							toVisit.add(current.up);
						}
						if (current.right != null
								&& !visited.contains(current.right.iIndex + ":" + current.right.jIndex)
								&& current.right.value == currentArea) {
							visited.add(current.right.iIndex + ":" + current.right.jIndex);
							toVisit.add(current.right);
						}
						if (current.down != null && !visited.contains(current.down.iIndex + ":" + current.down.jIndex)
								&& current.down.value == currentArea) {
							visited.add(current.down.iIndex + ":" + current.down.jIndex);
							toVisit.add(current.down);
						}
						if (current.left != null && !visited.contains(current.left.iIndex + ":" + current.left.jIndex)
								&& current.left.value == currentArea) {
							visited.add(current.left.iIndex + ":" + current.left.jIndex);
							toVisit.add(current.left);
						}

					}
				}

			}
		}

		boolean ret = true;
		for (int v : hash.keySet()) {
			if (hash.get(v) != 1) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	public boolean inRange(int i, int j, int[][] arr) {
		return i < arr.length && i >= 0 && j < arr[0].length && j >= 0;
	}

	public int[][] toDistribution() {
		int[][] dist = new int[this.map.length][this.map[0].length];
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[0].length; j++) {
				dist[i][j] = map[i][j].value;
			}
		}

		return dist;
	}

	

}
