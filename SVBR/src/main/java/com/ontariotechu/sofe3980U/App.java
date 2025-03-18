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

		//initialize variables to store the best model for each parameter
		String bestBCEModel = "";
		String bestAccuracyModel = "";
		String bestPrecisionModel = "";
		String bestRecallModel = "";
		String bestF1ScoreModel = "";
		String bestAUCModel = "";

		//initialize best values for comparison
		double bestBCE = Double.MAX_VALUE;
		double bestAccuracy = -Double.MAX_VALUE;
		double bestPrecision = -Double.MAX_VALUE;
		double bestRecall = -Double.MAX_VALUE;
		double bestF1Score = -Double.MAX_VALUE;
		double bestAUC = -Double.MAX_VALUE;

		//loop through each file to get data points
		for (String filePath : filePaths) {
			FileReader filereader;
			List<String[]> allData;
			try {
				filereader = new FileReader(filePath);
				CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
				allData = csvReader.readAll();
			} catch (Exception e) {
				System.out.println("Error reading the CSV file: " + e.getMessage());
				return;
			}

			// Initialize counters for confusion matrix and BCE calculation
			int tp = 0;
			int tn = 0;
			int fp = 0;
			int fn = 0;
			double bce = 0.0;

			// loop through each row to process the data and calculate parameters
			for (String[] row : allData) {
				int y_true = Integer.parseInt(row[0]);
				float y_predicted = Float.parseFloat(row[1]);

				//calculation for BCE loss, adding a small epsilon (1e-15) to avoid log(0) which is undefined
				bce += -(y_true * Math.log(y_predicted + 1e-15) + (1 - y_true) * Math.log(1 - y_predicted + 1e-15));

				//confusion matrix. The default value of the threshold is 0.5.
				if (y_true == 1 && y_predicted >= 0.5) {
					tp++;
				} else if (y_true == 0 && y_predicted < 0.5) {
					tn++;
				} else if (y_true == 0 && y_predicted >= 0.5) {
					fp++;
				} else if (y_true == 1 && y_predicted < 0.5) {
					fn++;
				}
			}

			//calculations for parameters
			int total = tp + fp + tn + fn;
			double accuracy = (tp + tn) / (double) total;
			double precision = tp / (double) (tp + fp + 1e-15);
			double recall = tp / (double) (tp + fn + 1e-15);
			double f1Score = 2 * (precision * recall) / (precision + recall + 1e-15);
			double aucRoc = (tp / (double) (tp + fn)) + (tn / (double) (tn + fp));
			aucRoc /= 1.0; // Simple ROC approximation
			bce /= total;


			//outputting results
			System.out.println("For " + filePath);
			System.out.printf("\nBCE = %.7f", bce);
			System.out.println("\nConfusion matrix");
			System.out.println("\ty=1\ty=0");
			System.out.printf("y^=1\t%d\t%d\n", tp, fp);
			System.out.printf("y^=0\t%d\t%d\n", fn, tn);
			System.out.printf("Accuracy = %.4f", accuracy);
			System.out.printf("\nPrecision = %.8f", precision);
			System.out.printf("\nRecall = %.8f", recall);
			System.out.printf("\nf1 score = %.8f", f1Score);
			System.out.printf("\nauc roc = %.8f\n\n\n\n", aucRoc);

			//decide best model according to each parameter
			if (bce < bestBCE) {
				bestBCE = bce;
				bestBCEModel = filePath;
			}
			if (accuracy > bestAccuracy) {
				bestAccuracy = accuracy;
				bestAccuracyModel = filePath;
			}
			if (precision > bestPrecision) {
				bestPrecision = precision;
				bestPrecisionModel = filePath;
			}
			if (recall > bestRecall) {
				bestRecall = recall;
				bestRecallModel = filePath;
			}
			if (f1Score > bestF1Score) {
				bestF1Score = f1Score;
				bestF1ScoreModel = filePath;
			}
			if (aucRoc > bestAUC) {
				bestAUC = aucRoc;
				bestAUCModel = filePath;
			}
		}

		//output result of finding best model
		System.out.println("\nAccording to BCE, The best model is " + bestBCEModel);
		System.out.println("\nAccording to Accuracy, The best model is " + bestAccuracyModel);
		System.out.println("\nAccording to Precision, The best model is " + bestPrecisionModel);
		System.out.println("\nAccording to Recall, The best model is " + bestRecallModel);
		System.out.println("\nAccording to F1 score, The best model is " + bestF1ScoreModel);
		System.out.println("\nAccording to AUC ROC, The best model is " + bestAUCModel);
	}
}
