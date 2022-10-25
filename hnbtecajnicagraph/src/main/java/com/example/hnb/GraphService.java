package com.example.hnb;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphService extends JPanel {

	private static final long serialVersionUID = 67973229392239941L;
	
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private int maxDataPoints = 60;
    
	private List<String> meanPrices;
	private List<String> dates;
   
	
	public GraphService() {
	}


	private GraphService(List<String> meanPrices, List<String> dates) {
		this.meanPrices = meanPrices;
		this.dates = dates;
	}
	
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double yScale = ((double) getHeight() - (2 * padding) - labelPadding) / (getMaxValue() - getMinScore());

        List<Point> meanPoints = new ArrayList<>();

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (dates.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);                
                String yLabel = ((int) ((getMinScore() + (getMaxValue() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // calculates ratio according to list size and specified maxDataPoints value
        int ratio = (int) (dates.size() / (maxDataPoints + 1) + 1);
        
        // create hatch marks and grid lines for x axis
        // also sets up point coordinates for values in the lists
        for (int i = 0; i < maxDataPoints; i++) {       	
            if (dates.size() > 1) {
                int x0 = i * ratio * (getWidth() - padding * 2 - labelPadding) / (maxDataPoints - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                
                if(i * ratio < dates.size()) {
                	int y2 = (int) ((getMaxValue() - Double.parseDouble(meanPrices.get(i * ratio))) * yScale + padding);
                    meanPoints.add(new Point(x1, y2));
                }else {
                	int y2 = (int) ((getMaxValue() - Double.parseDouble(meanPrices.get(dates.size() - 1))) * yScale + padding);
                    meanPoints.add(new Point(getWidth() - padding, y2));
                }
                
                //necessary so x-axis is not so cluttered with date values
                if(maxDataPoints >= 20 && i % (maxDataPoints / 20) != 0 && i != maxDataPoints - 1)
                	continue;
                
                //automatically places the last date in the range and its corresponding mean price on the last grid line of the graph
                if(i == maxDataPoints - 1 || i * ratio >= dates.size()) {
                	g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = dates.get(dates.size() - 1);
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                    break;
                    
                } else if(i * ratio < dates.size()) { //all other dates and mean prices are placed on the graph according to their value
                	g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = dates.get(i * ratio);
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }

                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();     
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < meanPoints.size() - 1; i++) {
            int x1 = meanPoints.get(i).x;
            int y1 = meanPoints.get(i).y;
            int x2 = meanPoints.get(i + 1).x;
            int y2 = meanPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < meanPoints.size(); i++) {
            int x = meanPoints.get(i).x - pointWidth / 2;
            int y = meanPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    
    private double getMinScore() {
    	double minScore = Double.MAX_VALUE;
        for (String el : meanPrices) {
            minScore = Math.min(minScore, Double.parseDouble(el));
        }
        return minScore - 0.05;
    }

    
    private double getMaxValue() {
        double maxScore = Double.MIN_VALUE;
        for (String el : meanPrices) {
            maxScore = Math.max(maxScore, Double.parseDouble(el));
        }
        return maxScore + 0.05;
    }

    
    public void createAndShowGui(List<String> meanPrices, List<String> dates, String currencyName) {
        GraphService mainPanel = new GraphService(meanPrices, dates);
        mainPanel.setPreferredSize(new Dimension(1600, 900));
        JFrame frame = new JFrame("Graf za valutu " + currencyName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
