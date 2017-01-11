import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;


 
public class GraphingData extends JPanel {
	
	public ArrayList<Double> x    = new ArrayList<>();	// x[]--> Bin 
	public ArrayList<Double> data = new ArrayList<>();	// y[]--> PDF of each bin in x[]	
    public double smallest_x;
    public double smallest_y; 
    public boolean dots;
    public boolean bars;
        												
    public GraphingData(){
		this.setBackground(Color.WHITE);
    } // Default Constructor. We don't use it but java wants it.
    
    // Default Constructor, Takes two arrays of values, one for x and the other for y
    // It also takes a character that indicates if the graph will be plotted linearly or lograrithmically 
    public GraphingData(ArrayList<Double> newX, ArrayList<Double> newY, char make_it_logarithmic, boolean newDots, boolean newBars){
    	this.dots = newDots;
    	this.bars = newBars;
		this.setBackground(Color.WHITE);
    	if(make_it_logarithmic == 'y'){ 		// SCALE VALUES FOR A LOGARITHMIC PLOT
			for(int z = 0; z<newX.size(); ++z){			// SCALE VALUES (LOG-LOG) AS THEY ARE COPIED
				this.x.add(Math.log10(newX.get(z))); 	// Scale x-values
			 	this.data.add(Math.log10(newY.get(z)));	// Scale y-values
    			}
    		this.findSmallest();						// SHIFT THE WHOLE GRAPH  to the
	    	for(int q = 0; q<this.x.size(); ++q){		// first quadrant of the cartesian plane
	    		this.x.set(q, (this.x.get(q)+smallest_x));
				this.data.set(q, (this.data.get(q)+smallest_y));
	    		
    			}
    		}
    	else{  // MAKE A LINEAR PLOT, Values are not scaled at all
    		this.x    = new ArrayList<Double>(newX);
			this.data = new ArrayList<Double>(newY);
    		}
    	}
    
    final int PAD = 50;	// Padding from the edge of the window to cartesian plane
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        
        
        // Draw Y-Axis
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw X-Axis.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        
        // Draw Labels
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        
        
        // Draw Y-Axis Label
        String s = "PDF";
        float sy = PAD + ((h - 2*PAD) - s.length()*sh)/2 + lm.getAscent(); // add these 5 lines to a for loop
        float sw1 = (float)font.getStringBounds(s, frc).getWidth();
        float sx1 = (PAD - sw1)/2;
        g2.drawString(s, sx1, sy);
        sy += sh;
        
        // Draw X-Axis Label
        s = "BIN";
        sy = h - PAD + (PAD - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);
		
		// Units of increment for X & Y values
        double xInc =  (double)(w - 2*PAD)/(getMaxX());
        double scale = (double)(h - 2*PAD)/getMax();
        
        
        if(this.bars){
	        // Draw Lines connecting dots in the plot
	        g2.setStroke(new BasicStroke(2));
	        Color lines = new Color(158, 213, 255);
	        g2.setPaint(lines); //Color.green.darker());
	        for(int i = 0; i < data.size()-1; i++) {
	            //double x1 = PAD + i*xInc;
	            double x1 = PAD + (xInc*this.x.get(i));
	            double y1 =h - PAD;// - scale*data.get(i);
	            double x2 = x1; //PAD + (this.x.get(i+1)*xInc);
	            double y2 = h - PAD - scale*data.get(i); // Point end
	            g2.draw(new Line2D.Double(x1, y1, x2, y2));
	        	}
        	}
        	
        
        
        
        if(this.dots){
	        // DRAW DATA POINTS in the graph (using only the First Quadrant)
	        Color dotColors = new Color(14,125,235);
	        g2.setPaint(dotColors);
	        
	        for(int i = 0; i < data.size(); i++) {
	            double x = PAD + (xInc*this.x.get(i));
	            double y = h - PAD - scale*data.get(i);
	            g2.fill(new Ellipse2D.Double(x-2, y-2, 5, 5)); // Plot at location x, y, sizex, sizey
	        	}
        }
    
    }
 	
 	// Set smallest X and Y value
 	private void findSmallest(){
    	this.smallest_x = this.x.get(0);
    	this.smallest_y = this.data.get(0);

		for(int i = 0; i<this.x.size(); ++i) {
   			if(this.x.get(i) < smallest_x ) smallest_x = this.x.get(i);
    	    if(this.data.get(i) < smallest_y ) smallest_y = this.data.get(i);
			}
		if(this.smallest_x<0.0)
			this.smallest_x = Math.abs(this.smallest_x );	
		else
			this.smallest_x *= (-1);	
		if(this.smallest_y<0.0)
			this.smallest_y = Math.abs(this.smallest_y );
		else
			this.smallest_y *= (-1);
	
		
 		}
 	
 	// Sets the smallest Y-value
    private double getMax() {
        double max = -Double.MAX_VALUE;
        for(int i = 0; i < data.size(); i++) {
            if(data.get(i) > max)
                max = data.get(i);
        }
        return max;
    }
    
    // Sets the smallest X-value
    private double getMaxX() {
        double max = -Double.MAX_VALUE;
        for(int i = 0; i < x.size(); i++) {
            if(x.get(i) > max)
                max = x.get(i);
        }
        return max;
    }
}