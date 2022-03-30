import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		//set-up Grid
		GridLayout grid = new GridLayout(16,16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		//create checkBoxes
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
			JMenu editMenu = new JMenu ("Edit");
			JMenu viewMenu = new JMenu ("View");

			JMenuItem newMenuItem = new JMenuItem("New");
			fileMenu.add(newMenuItem);
			
			menuBar.add(fileMenu);
			menuBar.add(editMenu);
			menuBar.add(viewMenu);
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
		
		JButton clear = new JButton ("Clear");
		clear.addActionListener(new MyClearListener());
		buttonBox.add(clear);
		
		JButton save = new JButton ("Save");
		save.addActionListener(new MySaveListener());
		buttonBox.add(save);
		
		JButton load = new JButton ("Load");
		load.addActionListener(new MyLoadListener());
		buttonBox.add(load);
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
	//start inner class
		public class MyClearListener implements ActionListener{
			public void actionPerformed(ActionEvent a) {
				for (int i=0;i<checkBoxList.size();i++) {
					checkBoxList.get(i).setSelected(false);
				}
			}
		}//close inner class
		//start inner class
		public class MySaveListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				boolean []checkBoxState = new boolean [256];
				for (int i=0;i<checkBoxState.length;i++) {
					JCheckBox check = (JCheckBox) checkBoxList.get(i);
					if (check.isSelected()) {
						checkBoxState[i]=true;
					}
				}//copy the checkBoxList

				try {
					FileOutputStream file = new FileOutputStream("fileName.ser");
					ObjectOutputStream o = new ObjectOutputStream (file);
					o.writeObject(checkBoxState);
					File f = new File(".");
					System.out.println(f.getAbsolutePath());
					o.close();
					
				}catch(Exception ee) {ee.printStackTrace();}
				
			}

		}
		
		public class MyLoadListener implements ActionListener{
			
			public void actionPerformed(ActionEvent e) {
				boolean [] checkBoxState = null;
				try {
					FileInputStream Load = new FileInputStream(new File("fileName.ser"));
					ObjectInputStream is = new ObjectInputStream(Load);
					checkBoxState = (boolean[]) is.readObject();
					
				}catch(Exception eee) {eee.printStackTrace();}
				for (int i=0;i<256;i++) {
					JCheckBox check = (JCheckBox) checkBoxList.get(i);
					if (checkBoxState[i]) {
						check.setSelected(true);
					}else {
						check.setSelected(false);
					}
				}
			}
			
		}
}
