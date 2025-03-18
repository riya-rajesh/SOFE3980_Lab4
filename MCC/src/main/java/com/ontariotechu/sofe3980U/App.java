package com.ontariotechu.sofe3980U;


import java.io.FileReader; 
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App 
{

	//additional method to help find the max values' index
	public static int maxIndex(float[] array) {
		int max = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[max]) {
				max = i;
			}
		}
		return max;
	}
    public static void main( String[] args )
    {
		String filePath="model.csv";
		FileReader filereader;
		List<String[]> allData;
		try{
			filereader = new FileReader(filePath); 
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build(); 
			allData = csvReader.readAll();
		}
		catch(Exception e){
			System.out.println( "Error reading the CSV file" );
			return;
		}


		int size = allData.size(); //total items
		int[][] confusionMatrix = new int[5][5]; //5x5 contribution matrix as expected
		float crossEntropy = 0.0f;


		//loop through and read each row in the file
		for (String[] row : allData) { 
			int y_true=Integer.parseInt(row[0])-1; //-1 to allign with 0-based index system

			//deal with predicted probabilities
			float[] y_predicted=new float[5];
			for(int i=0;i<5;i++){
				y_predicted[i]=Float.parseFloat(row[i+1]);
			}

			int predicted = maxIndex(y_predicted); //index of predicted class
			confusionMatrix[predicted][y_true]++; //update matrix
			crossEntropy += -Math.log(y_predicted[y_true]); //compute cross-entropy

		}

		//calculate and print the outputs
		crossEntropy /= size;
		System.out.println("\nCE = " + crossEntropy);
		System.out.println("\nConfusion matrix");
		System.out.println("\ty=1\ty=2\ty=3\ty=4\ty=5");
		for (int i = 0; i < 5; i++) {
			System.out.print("y^=" + (i + 1) + "\t");
			for (int j = 0; j < 5; j++) {
				System.out.print(confusionMatrix[i][j] + "\t");
			}
			System.out.println();
		}


	}
	
}
