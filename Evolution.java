package staticBridge.Cytotypes;

import java.util.HashMap;

public class Evolution {

	private double meiosisCoeff;
	private double mutationRate;
	private double probPolyploid;
	private double reducedFertilityTriploid;
	
	public Evolution(double meiosisCoeff, double mutationRate, double probPolyploid, double reducedFertilityTriploid) {
		
		this.meiosisCoeff = meiosisCoeff;
		this.mutationRate = mutationRate;
		this.probPolyploid = probPolyploid;
		this.reducedFertilityTriploid = reducedFertilityTriploid;
		
	}
	
	
	public int[][] meiosis(int[][] genome) {
		
		int numAlleles = (int) (genome[0].length*this.meiosisCoeff);
		int[][] gametes = null;
		
		switch (genome.length) {
		case 2:
			// Exchanges alleles between chromosomes. Exchange happens on a random linear sequence on the genome
			int marker = (int) (Math.random()*(genome[0].length - numAlleles));
			for(int i = marker; i < marker + numAlleles; i++) {
				int key = genome[0][i];
				genome[0][i] = genome[1][i];
				genome[1][i] = key;
			}
			double r = Math.random();
			
			if(r < this.probPolyploid) {

				gametes = new int[2][genome[0].length];
				for(int i = 0; i < genome[0].length; i++) {
					gametes[0][i] = genome[0][i];
					gametes[1][i] = genome[1][i];
				}
			}else {
				
				gametes = new int[1][genome[0].length];
				double pair = Math.random();
				if(pair < 0.5) {
					for(int i = 0; i < genome[0].length; i++) {
						gametes[0][i] = genome[0][i];
					}
				}else {
					for(int i = 0; i < genome[0].length; i++) {
						gametes[0][i] = genome[1][i];
					}
				}
			}
			break;
		case 4:

			int homolog01 = (int) (Math.random()*4.0);
			int homolog02 = (int) (Math.random()*4.0);
			
			while(homolog02 == homolog01) {
				homolog02 = (int) (Math.random()*4.0);
			}
			
			int homolog03 = 0;
			int homolog04 = 0;
			
			for(int i = 0; i < 4; i++) {
				if(i != homolog01 && i != homolog02) {
					homolog03 = i;
					break;
				}
			}
			
			for(int i = 0; i < 4; i++) {
				if(i != homolog01 && i != homolog02 && i != homolog03) {
					homolog04 = i;
					break;
				}
			}

			marker = (int) (Math.random()*(genome[0].length - numAlleles));

			for(int i = marker; i < marker + numAlleles; i++) {
				int key = genome[homolog01][i];
				genome[homolog01][i] = genome[homolog02][i];
				genome[homolog02][i] = key;
			}
			marker = (int) (Math.random()*(genome[0].length - numAlleles));

			for(int i = marker; i < marker + numAlleles; i++) {
				int key = genome[homolog03][i];
				genome[homolog03][i] = genome[homolog04][i];
				genome[homolog04][i] = key;
			}
			
				
			int gameteVector01 = 0;
			int gameteVector02 = 0;
				
			double t = Math.random();
			if(t < 0.5) {
				gameteVector01 = homolog01;
				gameteVector02 = homolog02;
			}else {
				gameteVector01 = homolog03;
				gameteVector02 = homolog04;
			}
				
			gametes = new int[2][genome[0].length];
			for(int i = 0; i < genome[0].length; i++) {
				gametes[0][i] = genome[gameteVector01][i];
			}

			for(int i = 0; i < genome[0].length; i++) {
				gametes[1][i] = genome[gameteVector02][i];
			}
			
			
			break;
		case 3:
			
			int bivalent01 = (int) (Math.random()*3.0);
			int bivalent02 = (int) (Math.random()*3.0);
			
			while(bivalent01 == bivalent02) {
				bivalent02 = (int) (Math.random()*3.0);
			}
			
			marker = (int) (Math.random()*(genome[0].length - numAlleles));

			for(int i = marker; i < marker + numAlleles; i++) {
				int key = genome[bivalent01][i];
				genome[bivalent01][i] = genome[bivalent02][i];
				genome[bivalent02][i] = key;
			}
			
			double aneuploid = Math.random();
			if(aneuploid < this.reducedFertilityTriploid) {
				gametes = null;
			}else {
				
				double euploid = Math.random();
				if(euploid < 0.5) {
					gametes = new int[2][genome[0].length];
					for(int i = 0; i < genome[0].length; i++) {
						gametes[0][i] = genome[bivalent01][i];
						gametes[1][i] = genome[bivalent02][i];
					}
				}else {
					gametes = new int[1][genome[0].length];
					for(int i = 0; i < genome[0].length; i++) {
						gametes[0][i] = genome[bivalent01][i];
					}
				}
			}
			break;
		}
		
		try {
			mutate(gametes);
		}catch(NullPointerException e) {
			
		}
		
		return gametes;
	}
	
	public double[] alleleFrequency(HashMap<Integer, int[][]> pool, int island) {
		
		double[] frequencies = new double[2]; // frequency of homozygotes [0] and heretozygotes [1]
		int diploids = 0;
		switch(island) {
		case 1:
			diploids = 0;
			for(int i = 0; i < pool.size(); i++) {
				try {
					if(pool.get(i).length == 2) {
						diploids++;
					}
				}catch(NullPointerException e) {
					
				}
			}
			for(int i = 0; i < pool.size(); i++) {
				try {
					if(pool.get(i).length == 2) {
						for(int j = 0; j < pool.get(i)[0].length; j++) {
							if(pool.get(i)[0][j] == 2 || pool.get(i)[1][j] == 3) {
								if(pool.get(i)[0][j] == pool.get(i)[1][j]) {
									frequencies[0] += 1.0/((double) diploids*pool.get(i)[0].length);
								}else {
									frequencies[1] += 1.0/((double) diploids*pool.get(i)[0].length);
								}
							}
						}
					}
					
				}catch(NullPointerException e) {
					
				}
			}
			break;
		case 2:
			diploids = 0;
			for(int i = 0; i < pool.size(); i++) {
				try {
					if(pool.get(i).length == 2) {
						diploids++;
					}
				}catch(NullPointerException e) {
					
				}
			}
			
			for(int i = 0; i < pool.size(); i++) {
				try {
					if(pool.get(i).length == 2) {
						for(int j = 0; j < pool.get(i)[0].length; j++) {
							if(pool.get(i)[0][j] == 0 || pool.get(i)[1][j] == 1) {
								if(pool.get(i)[0][j] == pool.get(i)[1][j]) {
									frequencies[0] += 1.0/((double) diploids*pool.get(i)[0].length);
								}else {
									frequencies[1] += 1.0/((double) diploids*pool.get(i)[0].length);
								}
							}
						}
					}
				}catch(NullPointerException e) {
					
				}
			}
			break;
		}
		return frequencies;
	}
	
	private void mutate(int[][] genome) {
		
		for(int i = 0; i < genome.length; i++) {
			for(int j = 0; j < genome[i].length; j++) {
				double r = Math.random();
				if(r < this.mutationRate) {
					boolean changed = false;
					while(changed == false) {
						int nucleotide = (int) (Math.random()*4);
						if(nucleotide != genome[i][j]) {
							genome[i][j] = nucleotide;
							changed = true;
						} 
					}			
				}
			}
		}
	}
	
}
