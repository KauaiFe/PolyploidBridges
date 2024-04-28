package staticBridge.Cytotypes;

import java.util.HashMap;

public class StartGenome {

	private int popSize;
	private int chromLength;
	private HashMap<Integer, int[][]> population;
	
	public StartGenome(int popSize, int chromLength) {
		
		this.popSize = popSize;
		this.chromLength = chromLength;
	}

	public HashMap<Integer, int[][]> startPop(int island) {
		
		HashMap<Integer, int[][]> population = new HashMap<>();
		
		switch(island) {
		case 1:
			for(int i = 0; i < this.popSize; i++) {
				int[][] genome = new int[2][this.chromLength];
				for(int j = 0; j < this.chromLength; j++) {
					genome[0][j] = 1;
					genome[1][j] = 1;
				}
				population.put(i, genome);
			}
			break;
		case 2:
			for(int i = 0; i < this.popSize; i++) {
				int[][] genome = new int[2][this.chromLength];
				for(int j = 0; j < this.chromLength; j++) {
					genome[0][j] = 0;
					genome[1][j] = 0;
				}
				population.put(i, genome);
			}
			break;
		}
		return population;
	}
	
}
