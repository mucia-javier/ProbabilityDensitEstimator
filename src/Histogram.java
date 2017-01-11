// LIBRARIES TO DEAL WITH FILES
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.util.Scanner;
import java.util.Arrays;




// DYNAMIC ARRAYS LIBRARY
import java.util.ArrayList;

public class Histogram {
	public ArrayList<Double> data = new ArrayList<>();	// List containing all the data in the file
	public double smallest_value;   					// The smallest value in the data-set from file
	public int number_of_values; 						// Number of values retrieved from the file
	
	public int number_of_bins;							// Number of Bins with a value > 0
	
	
	//	THE FOLLOWING TWO ARAYS HOLD THE RESULTS (to be saved in a new file)
	public ArrayList<Double> x = new ArrayList<>();		// x[]--> Bin 
	public ArrayList<Double> y = new ArrayList<>();		// y[]--> PDF of each bin in x[]
	
	
	//	DRIVER
	/*
	public static void main(String[] args){
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Source-file name (e.x. input.txt): ");
		String filename = keyboard.nextLine(); //"input.txt";

		double d1 = 0;
		System.out.print("Type samllest bin size (default 0): ");
		d1 = keyboard.nextDouble();
		Histogram h = new Histogram();

		h.getData(filename);	// Get data from specified file
		h.makePDF(d1);			// Calculate the PDF, given the smallest bin size
		h.saveResults(filename);// Saver results onto a new file (whose name is given as argument)
		}
	*/
	
	
	
	// Input: Takes the Coefficient to scale a set of logarithmic values, used as bin sizes
	// Output: Calculates the PDF of the probability of finding a value x between x and x+dx
	public void makePDF(double d1){
		if(d1==0)
			d1 = 2*this.smallest_value;
		
		for(int i1=0; i1<1000; ++i1){
			int pin = 0;
			int pout= 0;
			double d  = (d1*(Math.pow(2.0, i1))); //Make bins
			int[] bin = new int[21];
			double[] lbin = new double[21];
			for(int i = 0; i<21; ++i)
				lbin[i]= ((i+0.5)*d);
				
			for(int i2=0; i2<this.number_of_values; ++i2){ //Compute PDF
				int k = (int)(this.data.get(i2)/d);
				if(k>=20){
					bin[20] = (bin[20]+1);
					++pout;
					}
				else{
					bin[k] = (bin[k]+1);
					++pin;
					}				
				}
			
			for(int i3 = 1; i3<20; ++i3){ //Combine Histograms
				if(bin[i3]>0){
					++number_of_bins;
					this.x.add( lbin[i3] );
					this.y.add( (bin[i3]/(d*this.number_of_values)) );
					}
				}
			double btest = bin[1]*bin[2]*bin[3];
			if(btest ==0 ) break;
			}
	
		}
	
	
	
	
	
	
	
	
	
	
	// Input: the name of a file containing numeric values (one value per line)
	// Output: Constructs an array of Doubles, from the values on the file
	public void getData(String filename){
		try{
			Scanner f = new Scanner(new File(filename));
			int numberOfValues = 0; 					 // Count how many values are read from the file
			boolean is_first_value = true;
			
			while (f.hasNext()) { 						 // Checks each line in the file
    			String line = f.nextLine();				 // Retrieve stream as a string
    			if(!( line.isEmpty() || line == null) ){ // Read only lines with a value on them
    				++numberOfValues;
    				double current_value = Double.parseDouble(line); // Convert current Value, from string to a double
    				this.data.add(current_value); 		// Add the new numeric value to the array of data
    				
    				if(is_first_value){			  		// Keep track of the smallest value (used in bin size default)	
    					this.smallest_value = current_value;
    					is_first_value = false;
    					}
    				else if( current_value<this.smallest_value ){
    					this.smallest_value = current_value;
    					}    			
    				}   
				}
			f.close(); // Close file after all values have been copied to list "data"
			this.number_of_values = numberOfValues;
		    }
		 catch (IOException ioe) {
			ioe.printStackTrace();
			}
		}
	
	
	
	
		
	// Input: Takes a string that holds the name of the output file
	// Output: Saves the results of computations onto a new file 
	public void saveResults(String filename){
			try{
				File newFile = new File("PDF-"+filename);
				FileWriter fileWriter = new FileWriter(newFile);
				
				for(int i=0; i<x.size(); ++i){
					fileWriter.write(x.get(i)+","+y.get(i)+ "\n");
				}		
				fileWriter.flush();
				fileWriter.close();
				System.out.println("Results have been Saved!");
				
		    } catch (IOException ioe) { ioe.printStackTrace();
				}
		}
	
	
	
	}