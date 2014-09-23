package MovsimReportChart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.management.loading.PrivateClassLoader;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;



public class ReportChart extends JFrame implements ChangeListener, ItemListener{

	

	private JFreeChart chart;
	private ChartPanel chartPanel;
	private int TimeStep = 1;
	private List<ReportData> reportDataSets = new ArrayList<>();
	private String titleString;


	// Particle: 0, Real: 1, Simulated: 2
	private final Color [] colors = new Color []{new Color(50,0,200,50),new Color(255,0,0,200),new Color(0,150,50,200)} ; 
	private JCheckBox [] checkBoxs = new JCheckBox [] {new JCheckBox("Particles"),new JCheckBox("Real System"),new JCheckBox("Simulated System")};


	
	class ReportData {
		DefaultCategoryDataset particleSets = new DefaultCategoryDataset() ;
		DefaultCategoryDataset realSysSet = new DefaultCategoryDataset();
		DefaultCategoryDataset SimulatedSysSet = new DefaultCategoryDataset();
	}
	
	@SuppressWarnings("deprecation")
	public ReportChart(String title) {
		super(title);
		// TODO Auto-generated constructor stub		
		
			
		// Read Report files in directory 
		final File folder = new File("ParticleReports");
		readDataFromDirectory(folder);
		
		
		// create the chart using the report data
		chart = ChartFactory.createLineChart(title, "Road Segments", "Average speed", null);
		chart.getLegend().setVisible(false);
		
		// set up the chart preferences 
		initializeFrame();
		
		// bind the report data to the chart
		if (reportDataSets.size()>0) {
			setDataSetByStep(1);
			setDefaultRender();
		}
		
		
		// 
		
	}
	
	
	public ReportChart(String title, CategoryDataset dataset) {
		super(title);
		// TODO Auto-generated constructor stub		
		chart = ChartFactory.createLineChart(title, "Road Segments", "Average speed", dataset);
		
		initializeFrame();
            
	}
	
	private void initializeFrame() {
		setSize(800, 200);
		Container content = getContentPane(  );
		
		 JPanel main = new JPanel();
		 main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
		 chartPanel = new ChartPanel(chart);
	     chartPanel.setPreferredSize(new Dimension(800, 500));
	     // setContentPane(chartPanel);
	     final CategoryPlot plot = (CategoryPlot) chart.getPlot();
	     plot.setBackgroundPaint(Color.lightGray);
	     plot.setRangeGridlinePaint(Color.white);
		 main.add(chartPanel);
		    
		 JPanel sliderPanel = new JPanel(  );
		 
		 int min = 1;
		 int max = reportDataSets.size();
		 
		 
		 sliderPanel.setPreferredSize(new Dimension(800, 50));
		 final JSlider slider =
		     new JSlider(JSlider.HORIZONTAL, min, max, min );
		 slider.setMajorTickSpacing(5);
		 slider.setMinorTickSpacing(1);
		 slider.setPaintTicks(true);
		 slider.setPaintLabels(true);
		 slider.setPaintTrack(true);
		 slider.addChangeListener(this);
		 sliderPanel.add(slider);
		 
		 main.add(sliderPanel);
		
		 // add check box panel
		 JPanel checkboxPanel = new JPanel(  );
		 		 	 
		 for (int i = 0; i < 3; i++) {
			 checkboxPanel.add(checkBoxs[i]);
			 checkBoxs[i].setSelected(true);
			 checkBoxs[i].addItemListener(this);
		 }
		 
		 main.add(checkboxPanel);
		 
		 content.add(main, BorderLayout.NORTH);
		 content.setSize(800, 600);
		 
		this.setTitle(titleString);
	}
	
	private void readDataFromDirectory(File folder) {
		// Read all report files from the specified folder
		for (final File fileEntry : folder.listFiles()) {
			ReportData data = parseReportFiles(fileEntry);
			if (data!= null) {
				reportDataSets.add(data);
			}
	    }
	}
	private ReportData parseReportFiles(File reportFile) {
		
		// System.out.println(reportFile.getName());
		
		ReportData reportData = null;
		try {
			Scanner scanFile = new Scanner(new FileReader(reportFile));
			
			reportData = new ReportData();
						
			while (scanFile.hasNextLine()) {
				String str = scanFile.nextLine(); 
				if (str.compareTo("Particles:") == 0) {
					
					int num = scanFile.nextInt();
					scanFile.nextLine();
					for (int i = 0; i < num; i++) {
						Scanner dataScan = new Scanner(scanFile.nextLine());
						String series = String.format("Particle%d", i+1);
						
						List<Double> valueList = new ArrayList<Double>();
						int j = 0;
				        while (dataScan.hasNextDouble()) {
				        	double value = dataScan.nextDouble();
				            valueList.add(value);
				            String type = String.format("%02d", ++j);
				            // System.out.print(value+"\t");
				            reportData.particleSets.addValue(value, series, type);
				        }
				        
				        
					}
				}
				else if (str.compareTo("Real System:") == 0) {
					Scanner dataScan = new Scanner(scanFile.nextLine());
					int j = 0;
			        while (dataScan.hasNextDouble()) {			        	
			        	reportData.realSysSet.addValue(dataScan.nextDouble(), "Real System", String.format("%02d", ++j));
			        }
			        // System.out.println();
				}
				else if ( str.compareTo("Simulated System:") == 0){
					Scanner dataScan = new Scanner(scanFile.nextLine());
					int j = 0;
			        while (dataScan.hasNextDouble()) {			        	
			        	reportData.SimulatedSysSet.addValue(dataScan.nextDouble(), "Simulated System", String.format("%02d", ++j));
			        }
				}
				else if (str.compareTo("Test Settings") == 0) {
					titleString = "Test Setting:" + scanFile.nextLine() +" "+ scanFile.nextLine();
					
			        
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return reportData;
	}
	
	public static void main(String[] args){
		 // row keys...
      
       
		ReportChart chart = new ReportChart("test");
		
		
		
		
		chart.pack();	
		RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
		
	    // add a pie chart on the plot
	    
	}

	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	    	TimeStep = (int)source.getValue();
	        
	        // Display the page of the slider
	        setDataSetByStep(TimeStep);
	        for (int i = 0; i < 3; i++) {
	        	setDataVisible(i,checkBoxs[i].isSelected());
			}
	    }
	}
	
	
	
	
	private void setDataSetByStep(int step) {
		 chart.setTitle("Time step " + step);
		 chart.getCategoryPlot().setDataset(0, reportDataSets.get(step-1).particleSets);
		 chart.getCategoryPlot().setRenderer(0, new LineAndShapeRenderer());
		 chart.getCategoryPlot().setDataset(1, reportDataSets.get(step-1).realSysSet);
		 chart.getCategoryPlot().setRenderer(1, new LineAndShapeRenderer());
		 chart.getCategoryPlot().setDataset(2, reportDataSets.get(step-1).SimulatedSysSet);
		 chart.getCategoryPlot().setRenderer(2, new LineAndShapeRenderer());	
	}
	
	/**
	 * 
	 * @param dataset	indicate the dataset to be control
	 * @param selected	if true set the dataset to be visible
	 */
	private void setDataVisible(int dataset, Boolean selected) {
		
		
		// BarRenderer renderer = new BarRenderer();
		LineAndShapeRenderer  renderer = (LineAndShapeRenderer) chart.getCategoryPlot().getRenderer(dataset);
		int size = chart.getCategoryPlot().getDataset(dataset).getRowCount();
		Shape theShape=null;
		switch (dataset) {
		case 0:
			theShape = ShapeUtilities.createRegularCross(1, 1);
			break;
		case 1:
			theShape = ShapeUtilities.createDiagonalCross(2,1);
			break;
		case 2:
			theShape = ShapeUtilities.createUpTriangle(2);
			break;
			
		}
		
		for (int i = 0; i < size; i++) {
			
			renderer.setSeriesPaint(i,colors[dataset] );
			renderer.setSeriesShape(i, theShape);
			renderer.setSeriesShapesVisible(i, true);
			renderer.setSeriesVisible(i, selected);
		}
	}
	
	private void setDefaultRender() {
		
		LegendItemCollection legend = new LegendItemCollection();
		LegendItem li = new LegendItem("Particles", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, colors[0]);
	    legend.add(li);
	    li = new LegendItem("Real System", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, colors[1]);
	    legend.add(li);
	    li = new LegendItem("Simulated System", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, colors[2]);
	    legend.add(li);
		
	    chart.getCategoryPlot().setFixedLegendItems(legend);
	    chart.getLegend().setVisible(true);
	    chart.getLegend().setPosition(RectangleEdge.BOTTOM);
	    
	    
	    // Display the page of the slider
        setDataSetByStep(TimeStep);
        for (int i = 0; i < 3; i++) {
        	setDataVisible(i,checkBoxs[i].isSelected());
		}
	    
		
		
		
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		int dataset = -1;
		
		Object source = e.getItemSelectable();
		if (source == checkBoxs[0]) {
			dataset = 0; 
			
	    } else if (source == checkBoxs[1]) {
	    	dataset = 1; 
	    	    	
	    } else if (source == checkBoxs[2]) {
	    	dataset = 2;    
	    }
		
		setDataVisible(dataset,checkBoxs[dataset].isSelected());
		
	}
	
	
	private JFreeChart createReportChart() {
		JFreeChart fChart = null; 
		
		// final XYDataset data1 = createDataset1();
	    final XYItemRenderer renderer1 = new StandardXYItemRenderer();
	    final NumberAxis rangeAxis1 = new NumberAxis("Average Speed");
	    final CategoryPlot subplot1 = new CategoryPlot(null,null,rangeAxis1,null);
	    subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
	        
	    
	    // create subplot 2...
	    final NumberAxis rangeAxis2 = new NumberAxis("Obstacles");
	    rangeAxis2.setAutoRangeIncludesZero(false);
	      //  final XYPlot subplot2 = new XYPlot(data2, null, rangeAxis2, renderer2);
	      //  subplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
		
		
		return chart;
	}
	
}
