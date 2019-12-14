package icesi.vip.alien.alien.craftMethod.relocation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import icesi.vip.alien.alien.craftMethod.labelmap.LabelMap;
import icesi.vip.alien.alien.craftMethod.labelmap.LabelMapNode;

public class Relocation {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		int[][] facility = { 
				{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 }, 
				{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 },
				{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 },
				{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 }, 
				{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 },
				{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 }, 
				{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 }, 
				{ 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 },
				{ 1, 1, 1, 1, 1, 1, 3, 3, 3, 3 }, 
				{ 1, 1, 1, 1, 1, 1, 3, 3, 3, 3 }, 
				{ 1, 1, 1, 1, 1, 1, 3, 3, 3, 3 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

		LabelMap lm = new LabelMap(facility);
		printArray(facility);
		System.out.println();
		swap(facility, 1, 2,500);
		lm.checkValidity();

	}
	
	
	public static void printArray(int[][]matrix) {
		for (int i = 0; i < matrix.length; i++) {
		    for (int j = 0; j < matrix[i].length; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.println();
		}
	}

	public static int[][] clone(int[][] matrix) {
		int[][] copy = new int[matrix.length][matrix[0].length];
		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy[0].length; j++) {
				copy[i][j] = matrix[i][j];
			}
		}
		return copy;
	}

	private static int[][] swap_try(int[][] toSwap, ArrayList<String> startPoints, int small, int big, int toRelocate , int maxSwapTries) {
		Random rand = new Random();
		LabelMap lm;
		int relocateCount;
		int swapTry=0;
		do {

			int[][] current = clone(toSwap);
			int randind = rand.nextInt(startPoints.size());
			String[] nums = startPoints.get(randind).split(":");
			int i = Integer.parseInt(nums[0]);
			int j = Integer.parseInt(nums[1]);
			//System.out.println("Random pos:"+i+","+j);
			relocateCount = toRelocate;
			lm = new LabelMap(current);
			LinkedList<LabelMapNode> queue = new LinkedList<>();
			queue.add(lm.map[i][j]);
			if (rand.nextDouble() <= 0.5) {
				// desde up hacia la izq
				while (!queue.isEmpty() && relocateCount > 0) {
					
					LabelMapNode node = queue.poll();
					
					node.value = small;
					relocateCount--;
					boolean right=node.right != null && node.right.value==big && !queue.contains(node.right);
					boolean down=node.down != null && node.down.value==big && !queue.contains(node.down);
					boolean left=node.left != null && node.left.value==big && !queue.contains(node.left);
					boolean up=node.up != null && node.up.value==big && !queue.contains(node.up);
					
					if(up || down) {
						if(up) {
							queue.add(node.up);
						}
						if(down) {
							queue.add(node.down);
						}
					}else {
						if(right) {
							queue.add(node.right);
						}
						if(left) {
							queue.add(node.left);
						}
					}


				}

			} else {
				// desde up hacia la der
				while (!queue.isEmpty() && relocateCount > 0) {
					
					LabelMapNode node = queue.poll();
					
					node.value = small;
					relocateCount--;
					boolean right=node.right != null && node.right.value==big && !queue.contains(node.right);
					boolean down=node.down != null && node.down.value==big && !queue.contains(node.down);
					boolean left=node.left != null && node.left.value==big && !queue.contains(node.left);
					boolean up=node.up != null && node.up.value==big && !queue.contains(node.up);
					
					if(left || right) {
						if(right) {
							queue.add(node.right);
						}
						if(left) {
							queue.add(node.left);
						}
					}else {
						if(up) {
							queue.add(node.up);
						}
						if(down) {
							queue.add(node.down);
						}
					}



				}
			}

			for (int k = 0; k < lm.map.length; k++) {
				for (int k2 = 0; k2 < lm.map[0].length; k2++) {
					if (lm.map[k][k2].value == Integer.MIN_VALUE) {
						lm.map[k][k2].value = big;
					}
				}
			}
			//printArray(lm.toDistribution());
			//System.out.println();
			queue.clear();
			swapTry++;
			
			if(swapTry >= maxSwapTries) {
				System.out.println("Exiting after maximum swap tries: "+maxSwapTries);
				lm= new LabelMap(clone(toSwap));
				break;
			}
			
		} while (!lm.checkValidity() || relocateCount>0);

		return lm.toDistribution();

	}

	public static int[][] swap(int[][] facility, int area1, int area2, int maxSwapTries) throws Exception {
		if (new LabelMap(facility).checkValidity()) {

			int[][] toSwap = clone(facility);
			int a1Count = 0;
			int a2Count = 0;
			for (int i = 0; i < toSwap.length; i++) {
				for (int j = 0; j < toSwap[0].length; j++) {
					if (toSwap[i][j] == area1) {
						a1Count++;
					} else if (toSwap[i][j] == area2) {
						a2Count++;
					}
				}
			}

			if (a1Count > a2Count || a2Count > a1Count) {
				int big, small, toRelocate;
				if (a1Count > a2Count) {
					// area1 is greater

					big = area1;
					small = area2;

					toRelocate = a2Count;

				} else {
					// area2 is greater
					big = area2;
					small = area1;

					toRelocate = a1Count;
				}

				for (int i = 0; i < toSwap.length; i++) {
					for (int j = 0; j < toSwap[0].length; j++) {
						if (toSwap[i][j] == small) {
							toSwap[i][j] = Integer.MIN_VALUE;
						}
					}
				}

				ArrayList<String> arr = new ArrayList<String>();
				for (int i = 0; i < toSwap.length; i++) {
					for (int j = 0; j < toSwap[0].length; j++) {
						if (toSwap[i][j] == big) {
							arr.add(i + ":" + j);
						}
					}
				}
				
				return swap_try(toSwap, arr, small, big, toRelocate,maxSwapTries);

			} else {
				// easy case
				for (int i = 0; i < toSwap.length; i++) {
					for (int j = 0; j < toSwap[0].length; j++) {
						if(toSwap[i][j]==area1) {
							toSwap[i][j]=area2;
						}else if(toSwap[i][j]==area2) {
							toSwap[i][j]=area1;
						}
					}
				}
				
				return toSwap;
			}

		} else {
			throw new Exception("Given facility is illegal.");
		}

	}

}
