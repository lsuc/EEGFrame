/**
 * 
 */
package gui.Dialogs;


import features.output.ExtractUnivariateFeaturesController;
import features.output.Features;
import features.output.MultivariateFeatures;
import features.output.UnivariateFeatures;
import gui.EEGFrameMain;
import gui.SelectedSignal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;


/**
 * @author lsuc
 *
 */
public class ExtractUnivariateFeaturesWindow extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3103966359087334045L;
	
	private AnalysisUnivariateParametersDialog analysisParametersDialog;
	private LinearTimeFeaturesDialog linearTimeFeaturesDialog;
	private PhaseSpaceFeaturesDialog phaseSpaceFeaturesDialog;
	private EntropiesDialog entropiesDialog;
	private FractalFeaturesDialog fractalFeaturesDialog;
	private NonlinearOtherFeaturesDialog nonlinearOtherFeaturesDialog;
	private TimeFrequencyFeaturesDialog timeFrequencyFeaturesDialog;
	private LinearFrequencyFeaturesDialog linearFrequencyFeaturesDialog;
	private CsvForDataMiningDialog dataMiningCsvDialog;
	private JList signalsLabelList;
	private ExtractUnivariateFeaturesController univariateController;
	private JRadioButton appendButton, createButton; //, colonButton, semiColonButton;
	
	public ExtractUnivariateFeaturesWindow(ExtractUnivariateFeaturesController univariateController){
		EEGFrameMain.checkOnEventDispatchThread();
		this.univariateController = univariateController;
		this.analysisParametersDialog = new AnalysisUnivariateParametersDialog(this);
		this.dataMiningCsvDialog = new CsvForDataMiningDialog(univariateController);
		this.setTitle ("Features extraction");
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setPreferredSize(new Dimension(700,700));	
		this.setLocation(0, 0);	
		this.setLayout(new BorderLayout());
		JPanel panel = addFeaturesExtractionPanel();
		this.add(panel, BorderLayout.CENTER);
	    this.setResizable(false);
	    this.pack();
	}

	private JPanel addFeaturesExtractionPanel(){
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createRigidArea(new Dimension(5,10)));
		
		JPanel signalsLabelPanel = new JPanel();
		signalsLabelPanel.setLayout(new BoxLayout(signalsLabelPanel, BoxLayout.X_AXIS));
		JLabel signalsLabel = new JLabel("Signals for features extraction:");
		signalsLabelPanel.add(Box.createRigidArea(new Dimension(5,0)));
		signalsLabelPanel.add(signalsLabel);
		signalsLabelPanel.add(Box.createHorizontalGlue());
		
		panel.add(signalsLabelPanel);
		panel.add(Box.createRigidArea(new Dimension(5,5)));
		
		JPanel signalSelectionPanel = new JPanel();
		signalSelectionPanel.setLayout(new BoxLayout(signalSelectionPanel, BoxLayout.X_AXIS));
		signalsLabelList = new JList(new DefaultListModel());
//		signalsLabelList.setPreferredSize(new Dimension(50,10));
		signalsLabelList.setVisibleRowCount(5);
		signalsLabelList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane signalsLabelSrollPane = new JScrollPane(signalsLabelList);	
		signalsLabelSrollPane.setBorder(BorderFactory.createTitledBorder("Select signals for feature extraction: "));
		signalsLabelList.setCellRenderer(new DefaultListCellRenderer(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				SelectedSignal toBeRendered = (SelectedSignal) value;
				setText(toBeRendered.getFile().getName() +": " +toBeRendered.getSignalLabel());
				if(isSelected){
					setBackground(Color.LIGHT_GRAY);
				}
				else{
					setBackground(null);
				}
				return this;
	        }
		});
		signalSelectionPanel.add(Box.createHorizontalGlue());
		signalSelectionPanel.add(signalsLabelSrollPane);	
		signalSelectionPanel.add(Box.createHorizontalGlue());
		JButton selectAllSignalsButton = new JButton("Select All");
		selectAllSignalsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int start = 0;
			    int end = signalsLabelList.getModel().getSize() - 1;
			    if (end >= 0) {
			    	signalsLabelList.setSelectionInterval(start, end);
			    }
			}
		});
		signalSelectionPanel.add(selectAllSignalsButton);
		signalSelectionPanel.add(Box.createHorizontalGlue());
		panel.add(signalSelectionPanel);
		panel.add(Box.createRigidArea(new Dimension(5,10)));
		
		JPanel analysisParametersPanel = new JPanel();
		analysisParametersPanel.setLayout(new BoxLayout(analysisParametersPanel, BoxLayout.X_AXIS));
		JLabel analysisParametersLabel = new JLabel("Analysis parameters:");
		analysisParametersPanel.add(Box.createRigidArea(new Dimension(5,0)));
		analysisParametersPanel.add(Box.createHorizontalGlue());
		analysisParametersPanel.add(analysisParametersLabel);
		analysisParametersPanel.add(Box.createHorizontalGlue());
		JButton selectAnalysisParametersButton = new JButton("Select parameters");
		selectAnalysisParametersButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showAnalysisParametersDialog();
			}
		});
		analysisParametersPanel.add(selectAnalysisParametersButton);
		analysisParametersPanel.add(Box.createHorizontalGlue());
		
		panel.add(new JSeparator());
		panel.add(Box.createRigidArea(new Dimension(10,20)));
		panel.add(analysisParametersPanel);
		panel.add(Box.createRigidArea(new Dimension(10,20)));


		JPanel univariateFeaturesPanel = new JPanel();
		univariateFeaturesPanel.setLayout(new BoxLayout(univariateFeaturesPanel , BoxLayout.Y_AXIS));
		Border etchedUnivariate = BorderFactory.createEtchedBorder();
		Border titledUnivariate = BorderFactory.createTitledBorder(etchedUnivariate,"Univariate features");
		univariateFeaturesPanel .setBorder(titledUnivariate);
		panel.add(univariateFeaturesPanel);
		
		JPanel linearTimePanel = new JPanel();
		univariateFeaturesPanel.add(linearTimePanel);
		linearTimePanel.setLayout(new BoxLayout(linearTimePanel, BoxLayout.X_AXIS));
		JLabel linearTimeLabel = new JLabel("Linear time domain features");
		linearTimeLabel.setPreferredSize(new Dimension(300, 15));
		linearTimePanel.add(Box.createHorizontalGlue());
		linearTimePanel.add(linearTimeLabel);
		linearTimePanel.add(Box.createHorizontalGlue());
		JButton linearTimeButton = new JButton("Select");
		linearTimeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showLinearTimeFeaturesDialog();
				
			}
		});
		linearTimePanel.add(linearTimeButton);
		linearTimePanel.add(Box.createHorizontalGlue());
		univariateFeaturesPanel.add(Box.createRigidArea(new Dimension(5,10)));
			
		JPanel linearFrequencyPanel = new JPanel();
		univariateFeaturesPanel.add(linearFrequencyPanel);
		linearFrequencyPanel.setLayout(new BoxLayout(linearFrequencyPanel, BoxLayout.X_AXIS));
		JLabel linearFrequencyLabel = new JLabel("Linear frequency domain features");
		linearFrequencyLabel.setPreferredSize(new Dimension(300, 15));
		linearFrequencyPanel.add(Box.createHorizontalGlue());
		linearFrequencyPanel.add(linearFrequencyLabel);
		linearFrequencyPanel.add(Box.createHorizontalGlue());
		JButton linearFrequencyButton = new JButton("Select");
		linearFrequencyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showLinearFrequencyFeaturesDialog();				
			}
		});
		linearFrequencyPanel.add(linearFrequencyButton);
		linearFrequencyPanel.add(Box.createHorizontalGlue());
		univariateFeaturesPanel.add(Box.createRigidArea(new Dimension(5,10)));
	
		JPanel timeFrequencyPanel = new JPanel();
		univariateFeaturesPanel.add(timeFrequencyPanel);
		timeFrequencyPanel.setLayout(new BoxLayout(timeFrequencyPanel, BoxLayout.X_AXIS));
		JLabel timeFrequencyLabel = new JLabel("Time-frequency features");
		timeFrequencyLabel.setPreferredSize(new Dimension(300, 15));
		timeFrequencyPanel.add(Box.createHorizontalGlue());
		timeFrequencyPanel.add(timeFrequencyLabel);
		timeFrequencyPanel.add(Box.createHorizontalGlue());
		JButton timeFrequencyButton = new JButton("Select");
		timeFrequencyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showTimeFrequencyFeaturesDialog();
			}
		});
		timeFrequencyPanel.add(timeFrequencyButton);
		timeFrequencyPanel.add(Box.createHorizontalGlue());
		univariateFeaturesPanel.add(Box.createRigidArea(new Dimension(5,10)));

		JPanel nonlinearPhaseSpacePanel = new JPanel();
		univariateFeaturesPanel.add(nonlinearPhaseSpacePanel);
		nonlinearPhaseSpacePanel.setLayout(new BoxLayout(nonlinearPhaseSpacePanel, BoxLayout.X_AXIS));
		JLabel nonlinearPhaseSpaceLabel = new JLabel("Nonlinear - Phase space features");
		nonlinearPhaseSpaceLabel.setPreferredSize(new Dimension(300, 15));
		nonlinearPhaseSpacePanel.add(Box.createHorizontalGlue());
		nonlinearPhaseSpacePanel.add(nonlinearPhaseSpaceLabel);
		nonlinearPhaseSpacePanel.add(Box.createHorizontalGlue());
		JButton phaseSpaceFeaturesButton = new JButton("Select");
		phaseSpaceFeaturesButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showPhaseSpaceFeaturesDialog();				
			}
		});
		nonlinearPhaseSpacePanel.add(phaseSpaceFeaturesButton);
		nonlinearPhaseSpacePanel.add(Box.createHorizontalGlue());
		univariateFeaturesPanel.add(Box.createRigidArea(new Dimension(5,10)));
		
		JPanel nonlinearEntropyPanel = new JPanel();
		univariateFeaturesPanel.add(nonlinearEntropyPanel);
		nonlinearEntropyPanel.setLayout(new BoxLayout(nonlinearEntropyPanel, BoxLayout.X_AXIS));
		JLabel nonlinearEntropyLabel = new JLabel("Nonlinear - Entropy features");
		nonlinearEntropyLabel.setPreferredSize(new Dimension(300, 15));
		nonlinearEntropyPanel.add(Box.createHorizontalGlue());
		nonlinearEntropyPanel.add(nonlinearEntropyLabel);
		nonlinearEntropyPanel.add(Box.createHorizontalGlue());
		JButton nonlinearEntropyButton = new JButton("Select");
		nonlinearEntropyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showEntropiesDialog();
			}
		});
		nonlinearEntropyPanel.add(nonlinearEntropyButton);
		nonlinearEntropyPanel.add(Box.createHorizontalGlue());
		univariateFeaturesPanel.add(Box.createRigidArea(new Dimension(5,10)));	
		
		JPanel nonlinearFractalPanel = new JPanel();
		univariateFeaturesPanel.add(nonlinearFractalPanel);
		nonlinearFractalPanel.setLayout(new BoxLayout(nonlinearFractalPanel, BoxLayout.X_AXIS));
		JLabel nonlinearFractalLabel = new JLabel("Nonlinear - Fractal features");
		nonlinearFractalLabel.setPreferredSize(new Dimension(300, 15));
		nonlinearFractalPanel.add(Box.createHorizontalGlue());
		nonlinearFractalPanel.add(nonlinearFractalLabel);
		nonlinearFractalPanel.add(Box.createHorizontalGlue());
		JButton nonlinearFractalButton = new JButton("Select");
		nonlinearFractalButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showFractalFeaturesDialog();
			}
		});
		nonlinearFractalPanel.add(nonlinearFractalButton);
		nonlinearFractalPanel.add(Box.createHorizontalGlue());
		univariateFeaturesPanel.add(Box.createRigidArea(new Dimension(5,10)));
		
		JPanel nonlinearOtherPanel = new JPanel();
		univariateFeaturesPanel.add(nonlinearOtherPanel);
		nonlinearOtherPanel.setLayout(new BoxLayout(nonlinearOtherPanel, BoxLayout.X_AXIS));
		JLabel nonlinearOtherLabel = new JLabel("Nonlinear - Other features");
		nonlinearOtherLabel.setPreferredSize(new Dimension(300, 15));
		nonlinearOtherPanel.add(Box.createHorizontalGlue());
		nonlinearOtherPanel.add(nonlinearOtherLabel);
		nonlinearOtherPanel.add(Box.createHorizontalGlue());
		JButton nonlinearOtherButton = new JButton("Select");
		nonlinearOtherButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showNonlinearOtherFeaturesDialog();
			}
		});
		nonlinearOtherPanel.add(nonlinearOtherButton);
		nonlinearOtherPanel.add(Box.createHorizontalGlue());
		univariateFeaturesPanel.add(Box.createRigidArea(new Dimension(5,15)));
		
		JPanel dataMiningCsvPanel = new JPanel();
		dataMiningCsvPanel.setLayout(new BoxLayout(dataMiningCsvPanel, BoxLayout.X_AXIS));
		JLabel dataMiningCsvLabel = new JLabel("Csv file for data mining");
		dataMiningCsvLabel.setPreferredSize(new Dimension(300, 15));
		dataMiningCsvPanel.add(Box.createHorizontalGlue());
		dataMiningCsvPanel.add(dataMiningCsvLabel);
		dataMiningCsvPanel.add(Box.createHorizontalGlue());
		JButton dataMiningCsvButton = new JButton("Select");
		dataMiningCsvButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showdataMiningCsvDialog();				
			}
		});
		dataMiningCsvPanel.add(dataMiningCsvButton);
		dataMiningCsvPanel.add(Box.createHorizontalGlue());
		
		panel.add(Box.createRigidArea(new Dimension(5, 15)));	
		panel.add(dataMiningCsvPanel);
		panel.add(Box.createRigidArea(new Dimension(5,15)));	
		panel.add(new JSeparator());
		panel.add(Box.createRigidArea(new Dimension(5,15)));

		
		JPanel appendCreatePanel = new JPanel();
		appendCreatePanel.setLayout(new BoxLayout(appendCreatePanel, BoxLayout.X_AXIS));
		appendCreatePanel.add(Box.createHorizontalGlue());
		appendButton = new JRadioButton("Append features to existing file");
		appendCreatePanel.add(appendButton);
		appendCreatePanel.add(Box.createHorizontalGlue());
		createButton = new JRadioButton("Extract features to new file");
		appendCreatePanel.add(createButton);
		appendCreatePanel.add(Box.createHorizontalGlue());
	
		ButtonGroup appendCreateGroup = new ButtonGroup();
		appendCreateGroup.add(appendButton);
		appendCreateGroup.add(createButton);
		appendButton.setSelected(true);
		panel.add(Box.createRigidArea(new Dimension(5, 15)));	
		panel.add(appendCreatePanel);
		panel.add(Box.createRigidArea(new Dimension(5,15)));	
		panel.add(new JSeparator());
		panel.add(Box.createRigidArea(new Dimension(5,15)));	

//		JPanel separatorPanel = new JPanel();
//		separatorPanel.setLayout(new BoxLayout(appendCreatePanel, BoxLayout.X_AXIS));
//		separatorPanel.add(Box.createHorizontalGlue());
//		colonButton = new JRadioButton(",");
//		appendCreatePanel.add(colonButton);
//		appendCreatePanel.add(Box.createHorizontalGlue());
//		semiColonButton = new JRadioButton(";");
//		appendCreatePanel.add(semiColonButton);
//		appendCreatePanel.add(Box.createHorizontalGlue());
//	
//		ButtonGroup separatorGroup = new ButtonGroup();
//		separatorGroup.add(colonButton);
//		separatorGroup.add(semiColonButton);
//		colonButton.setSelected(true);
//		
//		
//		panel.add(Box.createRigidArea(new Dimension(5,15)));	
//		panel.add(new JSeparator());
//		panel.add(Box.createRigidArea(new Dimension(5,15)));	
		
		JPanel extractFeaturesPanel = new JPanel();
		panel.add(extractFeaturesPanel);
		extractFeaturesPanel.setLayout(new BoxLayout(extractFeaturesPanel, BoxLayout.X_AXIS));
		extractFeaturesPanel.add(Box.createHorizontalGlue());
		JButton resetSelectionButton = new JButton("Reset selection");
		extractFeaturesPanel.add(resetSelectionButton);
		extractFeaturesPanel.add(Box.createHorizontalGlue());
		JButton saveSelectionButton = new JButton("Save selection");
		extractFeaturesPanel.add(saveSelectionButton);
		extractFeaturesPanel.add(Box.createHorizontalGlue());
		JButton extractFeaturesButton = new JButton("Extract features");
		extractFeaturesButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				if(signalsLabelList.getSelectedValues() != null && signalsLabelList.getSelectedValues().length > 1){
//					
//					analysisMultivariateParametersDialog.saveInterval(multivariateController);
//					SelectedSignal[] signals = new SelectedSignal[signalsLabelList.getSelectedValues().length];
//					for(int i = 0; i < signalsLabelList.getSelectedValues().length; i++){
//						signals[i] = (SelectedSignal)signalsLabelList.getSelectedValues()[i];
//					}
//					multivariateController.getSelectedFeatures().get(0).setSignals(signals);
//					if(createButton.isSelected()){
////						multivariateController.beginFeatureExtraction(signals, false);
//					}
//					else {
////						multivariateController.beginFeatureExtraction(signals, true);
//					}
//					setVisible(false);
//					
//				}

				if(signalsLabelList.getSelectedValues() != null && signalsLabelList.getSelectedValues().length > 0){
					
					analysisParametersDialog.saveInterval(univariateController);
//					SelectedSignal[] signals = new SelectedSignal[signalsLabelList.getSelectedValues().length];
//					SelectedSignal[] multiSignals = new SelectedSignal[signalsLabelList.getSelectedValues().length];
					for(int i = 0; i < signalsLabelList.getSelectedValues().length; i++){
						SelectedSignal[] signals = new SelectedSignal[1];
						signals[0] = (SelectedSignal)signalsLabelList.getSelectedValues()[i];
//						multiSignals[i] = (SelectedSignal)signalsLabelList.getSelectedValues()[i];
						univariateController.getSelectedFeatures().get(0).getSignals().add(signals);
					}
					
					if(dataMiningCsvDialog.getdataMiningCheckBox().isSelected()){
						
						if(dataMiningCsvDialog.getMovingWindowCheckBox().isSelected()){
							if(analysisParametersDialog.getStartSampleButton().isSelected()){
								JOptionPane.showMessageDialog(null, "Interval should be selected in seconds for moving window option!\n " + "Remove interval selection in samples and select interval in seconds.");
								return;
							}
							if(!calculateIntervals()){
								return;
							}
								
							for(int i = 1; i < univariateController.getSelectedFeatures().size(); i++){
								for(int j = 0; j < univariateController.getSelectedFeatures().get(0).getSignals().size(); j++){
									SelectedSignal[] signals = univariateController.getSelectedFeatures().get(0).getSignals().get(j);
									univariateController.getSelectedFeatures().get(i).getSignals().add(signals);
								}
								HashMap<String, Boolean> features = univariateController.getSelectedFeatures().get(0).getFeatures();								
								univariateController.getSelectedFeatures().get(i).setFeatures(features);
							
							}
						
						}
						if(dataMiningCsvDialog.getMultivariateCheckBox().isSelected()){	
							univariateController.getExtractMixedFeaturesController().setOutputFileType(univariateController.DATA_MINING_CSV);

							saveMultivariateIntervals();
							for(int i = 1; i < univariateController.getExtractMixedFeaturesController().getExtractMultivariateFeaturesController().getSelectedFeatures().size(); i++){
								univariateController.getExtractMixedFeaturesController().getExtractMultivariateFeaturesController().getSelectedFeatures().get(i).setSignals(univariateController.getExtractMixedFeaturesController().getExtractMultivariateFeaturesController().getSelectedFeatures().get(0).getSignals());								
								HashMap<String, Boolean> features = univariateController.getExtractMixedFeaturesController().getExtractMultivariateFeaturesController().getSelectedFeatures().get(0).getFeatures();								
								univariateController.getExtractMixedFeaturesController().getExtractMultivariateFeaturesController().getSelectedFeatures().get(i).setFeatures(features);
							
							}
							if(dataMiningCsvDialog.getRemoveUnknownCheckBox().isSelected()){
								univariateController.removeUnknownOptions();
								univariateController.getExtractMixedFeaturesController().getExtractMultivariateFeaturesController().removeUnknownOptions();
							}
							if(createButton.isSelected()){
								univariateController.getExtractMixedFeaturesController().beginFeatureExtraction(false);
								setVisible(false);
								return;
							}
							else {
								univariateController.getExtractMixedFeaturesController().beginFeatureExtraction(true);
								setVisible(false);
								return;
							}
						}
						else{
							univariateController.setOutputFileType(univariateController.DATA_MINING_CSV);
						}
					}
					else{
						univariateController.setOutputFileType(univariateController.CSV);
					}
					
					if(createButton.isSelected()){
						univariateController.beginFeatureExtraction(false);
					}
					else {
						univariateController.beginFeatureExtraction(true);
					}
					setVisible(false);
					
				}	
				else{
					JOptionPane.showMessageDialog(null, "At least one signal should be selected!", "Extraction error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		extractFeaturesPanel.add(extractFeaturesButton);
		extractFeaturesPanel.add(Box.createHorizontalGlue());
		panel.add(Box.createRigidArea(new Dimension(5,15)));
		
		return panel;
	}
	private boolean calculateIntervals(){
		double window = Double.parseDouble(dataMiningCsvDialog.getMovingWindowSizeTextField().getText());
		int percent = (Integer)dataMiningCsvDialog.getPercentageSpinner().getValue();
		double overlappingSeconds = percent*window / 100;
		Double[] interval = univariateController.getSelectedFeatures().get(0).getTimeInterval().get(0);
		
		SelectedSignal s = univariateController.getSelectedFeatures().get(0).getSignals().get(0)[0];
		double minLength =  s.getFile().calculateDuration(s.getSignalIndex());
		for(int index = 1; index < univariateController.getSelectedFeatures().size(); index++){
			s = univariateController.getSelectedFeatures().get(index).getSignals().get(0)[0];	
			double length = s.getFile().calculateDuration(s.getSignalIndex());			
			if(length < minLength){
				minLength = length;
			}
		}
		if(interval[1] != 0){		
			if(Math.min(minLength,interval[1])-interval[0] < window){
				JOptionPane.showMessageDialog(null, "Moving window analysis for selected interval with selected parameters is impossible!\n"+"Size of window is larger than interval");
				return false;
			}
		}
		interval[1] = Math.min(minLength,interval[1]);
		Double[] timeFirst = new Double[2];
		timeFirst[0] = interval[0];
		timeFirst[1] = interval[0]+window;
		ArrayList<Double[]> timeIntervalFirst = new ArrayList<Double[]>();
		timeIntervalFirst.add(timeFirst);
		univariateController.getSelectedFeatures().get(0).setTimeInterval(timeIntervalFirst);
		
		
		Double[] time;
		for(double i = timeFirst[1]-overlappingSeconds; i+window <= interval[1]; i += (window-overlappingSeconds)){
			Features f = new UnivariateFeatures();
			ArrayList<Double[]> timeInterval = new ArrayList<Double[]>();
			time = new Double[2];
			time[0] = i;
			time[1] = i+window;
			timeInterval.add(time);
			f.setTimeInterval(timeInterval);
			univariateController.getSelectedFeatures().add(f);
		}
		
		return true;
	}
	public void saveMultivariateIntervals(){
		//GENERIRANJE SELECTED SIGNALA
		ArrayList<Double[]> timeIntervalFirst = univariateController.getSelectedFeatures().get(0).getTimeInterval();
		univariateController.getExtractMixedFeaturesController().getExtractMultivariateFeaturesController().getSelectedFeatures().get(0).setTimeInterval(timeIntervalFirst);
		for(int i = 1; i < univariateController.getSelectedFeatures().size(); i++){
			Features f = new MultivariateFeatures();
			ArrayList<Double[]> timeInterval = univariateController.getSelectedFeatures().get(i).getTimeInterval();
			f.setTimeInterval(timeInterval);
			univariateController.getExtractMixedFeaturesController().getExtractMultivariateFeaturesController().getSelectedFeatures().add(f);
		}
	}

	private synchronized void showAnalysisParametersDialog(){
		analysisParametersDialog.setLocationRelativeTo(this);
		analysisParametersDialog.setVisible(true);
	}
	
	private synchronized void showLinearTimeFeaturesDialog(){
		if(linearTimeFeaturesDialog == null){
			linearTimeFeaturesDialog = new LinearTimeFeaturesDialog(univariateController);
		}
		linearTimeFeaturesDialog.setLocationRelativeTo(this);
		linearTimeFeaturesDialog.setVisible(true);
	}
	private synchronized void showPhaseSpaceFeaturesDialog(){
		if(phaseSpaceFeaturesDialog == null){
			phaseSpaceFeaturesDialog = new PhaseSpaceFeaturesDialog(univariateController);
		}
		phaseSpaceFeaturesDialog.setLocationRelativeTo(this);
		phaseSpaceFeaturesDialog.setVisible(true);
	}
	
	private synchronized void showEntropiesDialog(){
		if(entropiesDialog == null){
			entropiesDialog = new EntropiesDialog(univariateController);
		}
		entropiesDialog.setLocationRelativeTo(this);
		entropiesDialog.setVisible(true);
	}
	
	private synchronized void showFractalFeaturesDialog(){
		if(fractalFeaturesDialog == null){
			fractalFeaturesDialog = new FractalFeaturesDialog(univariateController);
		}
		fractalFeaturesDialog.setLocationRelativeTo(this);
		fractalFeaturesDialog.setVisible(true);
	}
	
	private synchronized void showNonlinearOtherFeaturesDialog(){
		if(nonlinearOtherFeaturesDialog == null){
			nonlinearOtherFeaturesDialog = new NonlinearOtherFeaturesDialog(univariateController);
		}
		nonlinearOtherFeaturesDialog.setLocationRelativeTo(this);
		nonlinearOtherFeaturesDialog.setVisible(true);
	}
	
	private synchronized void showTimeFrequencyFeaturesDialog(){
		if(timeFrequencyFeaturesDialog == null){
			timeFrequencyFeaturesDialog = new TimeFrequencyFeaturesDialog(univariateController);
		}
		timeFrequencyFeaturesDialog.setLocationRelativeTo(this);
		timeFrequencyFeaturesDialog.setVisible(true);
	}
	
	private synchronized void showLinearFrequencyFeaturesDialog(){
		if(linearFrequencyFeaturesDialog == null){
			linearFrequencyFeaturesDialog = new LinearFrequencyFeaturesDialog(univariateController);
		}
		linearFrequencyFeaturesDialog.setLocationRelativeTo(this);
		linearFrequencyFeaturesDialog.setVisible(true);
	}
	private synchronized void showdataMiningCsvDialog(){
		dataMiningCsvDialog.setLocationRelativeTo(this);
		dataMiningCsvDialog.setVisible(true);
	}
	public AnalysisUnivariateParametersDialog getAnalysisParametersDialog() {
		return analysisParametersDialog;
	}

	public void setAnalysisParametersDialog(AnalysisUnivariateParametersDialog analysisParametersDialog) {
		this.analysisParametersDialog = analysisParametersDialog;
	}

	public LinearTimeFeaturesDialog getLinearTimeFeaturesDialog() {
		return linearTimeFeaturesDialog;
	}

	public void setLinearTimeFeaturesDialog(LinearTimeFeaturesDialog linearTimeFeaturesDialog) {
		this.linearTimeFeaturesDialog = linearTimeFeaturesDialog;
	}

	public PhaseSpaceFeaturesDialog getPhaseSpaceFeaturesDialog() {
		return phaseSpaceFeaturesDialog;
	}

	public void setPhaseSpaceFeaturesDialog(PhaseSpaceFeaturesDialog phaseSpaceFeaturesDialog) {
		this.phaseSpaceFeaturesDialog = phaseSpaceFeaturesDialog;
	}

	public JList getSignalsLabelList() {
		return signalsLabelList;
	}

	public void setSignalsLabelList(JList signalsLabelList) {
		this.signalsLabelList = signalsLabelList;
	}
	public void setSignalsLabelList(SelectedSignal[] signals) {
		DefaultListModel signalsLabelModel = (DefaultListModel) this.signalsLabelList.getModel();
		signalsLabelModel.clear();
		for(int i = 0; i < signals.length; i++){
			signalsLabelModel.addElement(signals[i]);
		}
	}
	public SelectedSignal[] getSignalsList(){
		DefaultListModel signalsLabelModel = (DefaultListModel) this.signalsLabelList.getModel();
		SelectedSignal[] signals = new SelectedSignal[signalsLabelModel.size()];
		for(int i = 0; i < signals.length; i++){
			signals[i] = (SelectedSignal) signalsLabelModel.get(i);
		}
		return signals;
	}
	
	public ExtractUnivariateFeaturesController getUnivariateController() {
		return univariateController;
	}

	public void setUnivariateController(ExtractUnivariateFeaturesController univariateController) {
		this.univariateController = univariateController;
	}

	public EntropiesDialog getEntropiesDialog() {
		return entropiesDialog;
	}

	public void setEntropiesDialog(EntropiesDialog entropiesDialog) {
		this.entropiesDialog = entropiesDialog;
	}

	public FractalFeaturesDialog getFractalFeaturesDialog() {
		return fractalFeaturesDialog;
	}

	public void setFractalFeaturesDialog(FractalFeaturesDialog fractalFeaturesDialog) {
		this.fractalFeaturesDialog = fractalFeaturesDialog;
	}

	public NonlinearOtherFeaturesDialog getNonlinearOtherFeaturesDialog() {
		return nonlinearOtherFeaturesDialog;
	}

	public void setNonlinearOtherFeaturesDialog(
			NonlinearOtherFeaturesDialog nonlinearOtherFeaturesDialog) {
		this.nonlinearOtherFeaturesDialog = nonlinearOtherFeaturesDialog;
	}

	public TimeFrequencyFeaturesDialog getTimeFrequencyFeaturesDialog() {
		return timeFrequencyFeaturesDialog;
	}

	public void setTimeFrequencyFeaturesDialog(
			TimeFrequencyFeaturesDialog timeFrequencyFeaturesDialog) {
		this.timeFrequencyFeaturesDialog = timeFrequencyFeaturesDialog;
	}	

	public LinearFrequencyFeaturesDialog getLinearFrequencyFeaturesDialog() {
		return linearFrequencyFeaturesDialog;
	}

	public void setLinearFrequencyFeaturesDialog(
			LinearFrequencyFeaturesDialog linearFrequencyFeaturesDialog) {
		this.linearFrequencyFeaturesDialog = linearFrequencyFeaturesDialog;
	}

	public CsvForDataMiningDialog getdataMiningCsvDialog() {
		return dataMiningCsvDialog;
	}

	public void setdataMiningCsvDialog(CsvForDataMiningDialog dataMiningCsvDialog) {
		this.dataMiningCsvDialog = dataMiningCsvDialog;
	}

}
