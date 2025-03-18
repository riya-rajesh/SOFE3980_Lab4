package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App {
	public static void main(String[] args) {
		String[] filePaths = {"model_1.csv", "model_2.csv", "model_3.csv"};

		//keeps track of the best models for each type of error
		String bestModelMSE = "";
		String bestModelMAE = "";
		String bestModelMARE = "";

		//keeps track of the lowest value of respective error types when comparing all 3 models
		//initialize with high values for easier comparison
		double lowestMSE = Double.MAX_VALUE;
		double lowestMAE = Double.MAX_VALUE;
		double lowestMARE = Double.MAX_VALUE;


		//loop through each file to get data and compute error values
		for (String filePath : filePaths) {
			FileReader filereader;
			List<String[]> allData;
			try {
				filereader = new FileReader(filePath);
				CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
				allData = csvReader.readAll();
				csvReader.close();

				int count = 0;
				double mse = 0;
				double mae = 0;
				double mare = 0;

				//loop through each row in the file
				for (String[] row : allData) {
					float y_true = Float.parseFloat(row[0]);
					float y_predicted = Float.parseFloat(row[1]);
					float error = y_true - y_predicted;

					//calculation for MSE
					mse += error * error;
					//calculation for MAE
					mae += Math.abs(error);
					//calculation for MARE
					if (y_true != 0) {
						mare += Math.abs(error / y_true);
					}
					count++;

				}

				//final average calculations for each error type per model
				mse /= count;
				mae /= count;
				mare /= count;

				//output the found values
				System.out.println("For " + filePath);
				System.out.println("\nMSE = " + mse);
				System.out.println("\nMAE = " + mae);
				System.out.println("\nMARE = " + mare);
				System.out.println("\n\n");

				//update the lowest value of each error type if needed to make sure best model is found
				if (mse < lowestMSE) {
					lowestMSE = mse;
					bestModelMSE = filePath;
				}
				if (mae < lowestMAE) {
					lowestMAE = mae;
					bestModelMAE = filePath;
				}
				if (mare < lowestMARE) {
					lowestMARE = mare;
					bestModelMARE = filePath;
				}

			} catch (Exception e) {
				System.out.println("Error reading the CSV file: " + filePath);
			}
		}

		//print best model for each error type
		System.out.println("According to MSE, The best model is " + bestModelMSE);
		System.out.println("According to MAE, The best model is " + bestModelMAE);
		System.out.println("According to MARE, The best model is " + bestModelMARE);
	}
}
