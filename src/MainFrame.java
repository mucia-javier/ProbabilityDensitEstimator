import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

	// Upper Pannel used to get: filename, d1, graphType, etc.
	class Uservalues extends JPanel {
		
		// Filename Label and Text-Field
		JLabel fileName_label = new JLabel("Name of File: ");
		JTextField fileName_field = new JTextField("input.txt", 20);

		// D1-value, label and text field
		JLabel d1_label = new JLabel("Value for d1: ");
		JTextField d1_field = new JTextField("0", 12);

		// Radio Buttons that let the user select either a Linear or Logarithmic Plot
		JRadioButton linearPlot = new JRadioButton("Linear Plot", true);
        JRadioButton logPlot = new JRadioButton("Log-Log Plot");
		ButtonGroup radioGroup = new ButtonGroup();

		// CheckBoxes that let the user select graphing options
	    JCheckBox savePDF = new JCheckBox("Save PDF(x) to new file", false);
	    JCheckBox scatterDots = new JCheckBox("Scatter Dots", true);
	    JCheckBox bars = new JCheckBox("Bars", false);
	    
	    //Button that initilizes calculations and draws a graph with the results 
	    // Given the values on the above fields
		JButton computeButton = new JButton("Compute");
		
		// Provides user feedback on the selection of values
		JLabel error_label = new JLabel("");


		// CONSTRUCTOR for the upper pannel to get user values
		public Uservalues() {
			setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			
        	radioGroup.add(linearPlot);
        	radioGroup.add(logPlot);
        	
         	//// First column /////////////////////////
   			gc.anchor = GridBagConstraints.LINE_END; //THis is like "Align Right" content
   			gc.weightx = 0.5;
   			gc.weighty = 0.5;
   			
   			gc.gridx = 0;
   			gc.gridy = 0;
   			add(fileName_label, gc);

   			gc.gridx = 0;
   			gc.gridy = 1;
   			add(d1_label, gc);	
   			
   			
   			//// Second column
   			gc.anchor = GridBagConstraints.LINE_START; // This is like "Align Left" content
   			gc.gridx = 1;
   			gc.gridy = 0;
   			add(fileName_field, gc);

   			gc.gridx = 1;
   			gc.gridy = 1;
   			gc.ipadx = 50;
   			add(d1_field, gc);

   			gc.gridx = 1;
   			gc.gridy = 2;
   			add(scatterDots, gc);
   			
   			gc.gridx = 1;
   			gc.gridy = 3;
   			add(bars, gc);
   			
   			gc.gridx = 1;
   			gc.gridy = 4;
   			add(savePDF, gc);
   			
   			//// Third Column 
   			gc.gridx = 2;
   			gc.gridy = 2;
   			add(linearPlot, gc);
   			
   			gc.gridx = 2;
   			gc.gridy = 3;
   			add(logPlot, gc);

   			gc.gridx = 2;
   			gc.gridy = 4;
   			add(computeButton, gc);
   			
   			
   			// User-Selection feedback Text at the bottom of the upper panel
   			gc.anchor = GridBagConstraints.CENTER;
   			gc.gridheight = 2;
   			gc.gridx = 1;
   			gc.gridy = 5;
   			gc.gridwidth = 2;
   			add(error_label, gc);        	
        	
   			
   			// LISTENERS for the Radio Buttons, CheckBoxes, etc on the upper pannel
        	linearPlot.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {         
				error_label.setText("Linear Plot selected");
				error_label.setVisible(true);
				log_log_graph = 'n';
         		}           
      		});

        	logPlot.addItemListener(new ItemListener() {
        		public void itemStateChanged(ItemEvent e) { 
				error_label.setText("Logarithmic plot selected");
				error_label.setVisible(true); 
				log_log_graph = 'y';
         		}           
      		});

        	savePDF.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {         

				if(savePDF.isSelected()){
					save_to_PDF = true;
					error_label.setText("Results will be saved to new file");
					}
				else{
					save_to_PDF = false;
					error_label.setText("Results will NOT be saved to new file");
					}
				error_label.setVisible(true);
				}           
      		});
        	
			if(scatterDots.isSelected()){
				error_label.setText("Scatter Dots Enabled");
				dotsOnGraph=true;
				}
    	
        	scatterDots.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {         
				dotsOnGraph=true;
				if(!(scatterDots.isSelected())){
					error_label.setText("Scatter Dots Disabled");
					dotsOnGraph = false;
					}
				else{
					error_label.setText("Scatter Dots Enabled");
					dotsOnGraph=true;
					}
				error_label.setVisible(true);
				}           
      		});        	
        		
        	bars.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {         

				if(bars.isSelected()){
					error_label.setText("Bars Enabled");
					barsOnGraph = true;
					}
				else{
					error_label.setText("Bars Disabled");
					barsOnGraph = false;
					}
				error_label.setVisible(true);
				}           
      		});
        	
        	// "COMPUTE" button calculates results and graphs results
			computeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					error_label.setText("File: "+fileName_field.getText()+", d1="+d1_field.getText()+", log-log Graph: "+log_log_graph);
					error_label.setVisible(true);

					theHistogram = new Histogram();
					String fn = fileName_field.getText();
					theHistogram.getData(fileName_field.getText());
					
					String d1String = d1_field.getText();
					double d1 = Double.parseDouble(d1String);
					theHistogram.makePDF(d1);
					if( save_to_PDF==true )
						theHistogram.saveResults(fn);
					
					graph = new GraphingData(theHistogram.x, theHistogram.y, log_log_graph, dotsOnGraph, barsOnGraph);
					
					c.remove(UservaluesPanel);
					c.remove(graph);
					c.revalidate();
					
					c.add(UservaluesPanel, BorderLayout.NORTH);
					c.add(graph, BorderLayout.CENTER);
					c.revalidate();
				}
			});	
		}
	}



	final Container c;
	final Uservalues UservaluesPanel;
	public GraphingData graph; 
	public Histogram theHistogram;
	public char log_log_graph;
	public boolean save_to_PDF; 
	public boolean dotsOnGraph;
	public boolean barsOnGraph;
	

	public MainFrame(String title) {
		// Set the initial Size of the window for the progam
		super(title);
		this.setSize(650, 550); // (X_size, Y_size)
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		setLayout(new BorderLayout()); // Set layout manager
		
		// Add Swing components to content pane
		c = getContentPane();
		
		// Pannel to get user values
		UservaluesPanel = new Uservalues();
		c.add(UservaluesPanel, BorderLayout.NORTH);

		// Region where the graph will be drawn
		graph = new GraphingData();
		c.add(graph, BorderLayout.CENTER);
	}
	    public static void main(String[] args) {
 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new MainFrame("Mucia_Jaiver, Project 1");
            }
        });
    }
}
