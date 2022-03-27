import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class GUI {
	JFrame frame;
	MidiController midiEv;
	JPanel mainPanel, background;
	ArrayList <JCheckBox> checkBoxList; //will store the checkboxes
	Box buttonBox;
	
	public GUI () {
		buildGui();
	}
	
	public void buildGui() {	
		initialize();
		setMenuBar();
		
		String []instrumentNames = new DataHolder().getInstrumentName();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		checkBoxList = new ArrayList<JCheckBox>();
		buttonBox = new Box(BoxLayout.Y_AXIS);
		
		//SET UP BUTTONS
		setBtn();
		
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		
		for(int i=0;i<instrumentNames.length;i++) {
			nameBox.add(new Label(instrumentNames[i]));
		}
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		frame.getContentPane().add(background);
		
		GridLayout grid = new GridLayout(16,16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		
		for (int i=0;i<instrumentNames.length*16;i++) {
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(false);
			checkBoxList.add(checkBox);
			mainPanel.add(checkBox);
		}
		midiEv = new MidiController(checkBoxList);
		frame.setBounds(50,50,300,300);
		frame.pack();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame ("Cyber Beatbox");
		BorderLayout layout = new BorderLayout();
		background = new JPanel (layout);
	
	}		

	private void setMenuBar() {
	//set-up and add menuBar
			JMenuBar menuBar = new JMenuBar();
			JMenu fileMenu = new JMenu("File");
			JMenuItem newMenuItem = new JMenuItem("New");
			JMenuItem editMenuItem = new JMenuItem("Edit");
			fileMenu.add(newMenuItem);
			fileMenu.add(editMenuItem);
			menuBar.add(fileMenu);
			frame.setJMenuBar(menuBar);
	}

	private void setBtn() {
		JButton start = new JButton ("Start");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);
		
		JButton stop = new JButton ("Stop");
		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		JButton tempoUp = new JButton ("Tempo Up");
		tempoUp.addActionListener(new MyTempoUpListener());
		buttonBox.add(tempoUp);
		
		JButton tempoDown = new JButton ("Tempo Down");
		tempoDown.addActionListener(new MyTempoDownListener());
		buttonBox.add(tempoDown);
	}

//Buttons action
//start inner class
	public class MyStartListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			midiEv.buildTrackAndStart();
		}
	}//close inner class
	//start inner class
	public class MyStopListener implements ActionListener{
		public void actionPerformed(ActionEvent b) {
			midiEv.stop();
		}
	}//close inner class
	//start inner class
	public class MyTempoUpListener implements ActionListener{
		public void actionPerformed(ActionEvent c) {
			midiEv.tempoUp();
		}
	}//close inner class
	//start inner class
	public class MyTempoDownListener implements ActionListener{
		public void actionPerformed(ActionEvent d) {
			midiEv.tempoDown();
		}
	}//close inner class
}
