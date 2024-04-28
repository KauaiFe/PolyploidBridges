package staticBridge.Cytotypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Bridge {

	public static void main(String[] args) throws IOException {
		
		int generations = 150;
		int popSize = 1000;
		int chromLength = 100;
		double meiosisCoeff = 0.05;
		double mutationRate = 0.0;
		double probPolyploid = 0.1215;
		double reducedFertilityTriploid = 0.85;
		
		File file = new File("Cytotypes_Dynamics_unreducedGametes01215_Triploids015.txt");
		FileWriter fw = new FileWriter(file);
		PrintWriter cytotypes = new PrintWriter(fw);
		cytotypes.println("diploids;triploids;tetraploids;g");
		
		StartGenome initiate = new StartGenome(popSize, chromLength);
		Evolution resolve = new Evolution(meiosisCoeff, mutationRate, probPolyploid, reducedFertilityTriploid);
		HashMap<Integer, int[][]> pool01 = initiate.startPop(1);

		initiate = new StartGenome(popSize, chromLength);
		resolve = new Evolution(meiosisCoeff, mutationRate, probPolyploid, reducedFertilityTriploid);
		
		
		int rep = 0;
		while(rep < 10) {
			//probPolyploid = 0.0;
			System.out.println("Repetition: "+rep);
			//while(probPolyploid < 0.30001) {
				
			pool01 = initiate.startPop(1);
				
				double tetraploids = 0.0;
				double triploids = 0.0;
				double diploids = 0.0;
				double sum = 0.0;
					
				int count = 0;
				while(count < generations) {
					
					HashMap<Integer, int[][]> newPool01 = new HashMap<>();
					ArrayList<Integer> triploids01 = new ArrayList<>();
					ArrayList<Integer> tetraploids01 = new ArrayList<>();
					
					int offspring01Id = 0;
					while(newPool01.size() < popSize) {
						
						int mate01 = (int) (Math.random()*pool01.size());
						int mate02 = (int) (Math.random()*pool01.size());
						
						try {
							int[][] gamete01 = resolve.meiosis(pool01.get(mate01));
							int[][] gamete02 = resolve.meiosis(pool01.get(mate02));
							int[][] offspring = null;
							//if(gamete01.length == gamete02.length) {
								offspring = joinGametes(gamete01, gamete02);
								newPool01.put(offspring01Id, offspring);
								offspring01Id++;
							//}
							
							if(offspring.length == 4) {
								tetraploids01.add(offspring01Id);
								
							}else if(offspring.length == 3) {
								triploids01.add(offspring01Id);
							}
						}catch(NullPointerException e) {
							
						}
					}
					
					pool01 = newPool01;
					tetraploids = tetraploids01.size();
					triploids = triploids01.size();
					diploids = popSize - triploids - tetraploids;

					//if(count == 4999) {
					
						sum = diploids + triploids + tetraploids;
						
						cytotypes.println((diploids/sum)+";"+(triploids/sum)+";"+(tetraploids/sum)+";"+count);
					//}
						System.out.println((diploids/sum)+";"+(triploids/sum)+";"+(tetraploids/sum)+";"+probPolyploid+"\t"+count);
					count++;
				}
				
			//}
			
			rep++;
		}
		
		cytotypes.close();
	}

	static int[][] joinGametes(int[][] gametes01, int[][] gametes02) {
		
		int ploidy = gametes01.length + gametes02.length;
		int[][] newGenome = new int[ploidy][gametes01[0].length];

		for(int j = 0; j < gametes01.length; j++) {
			for(int k = 0; k < gametes01[j].length; k++) {
				newGenome[j][k] = gametes01[j][k];
			}
		}
		for(int j = gametes01.length; j < ploidy; j++) {
			for(int k = 0; k < gametes02[0].length; k++) {
				newGenome[j][k] = gametes02[j-gametes01.length][k];
			}
		}
		
		return newGenome;
	}
}

