import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;

public class HeadsFirstJukebox {
	JPanel mainPanel;
	ArrayList <JCheckBox> checkBoxList; //will store the checkboxes
	Sequencer seqr;
	Sequence seq;
	Track tr=null;
	JFrame frame;
	
	String []instrumentNames = new String[]{"Bass Drum","Closed Hi-Hat",
			"Open Hi-Hat","Acoustic Snare","Crash Cymbal",
			"Hand Clap","High Tom", "High Bongo","Maracas",
			"Whistle","Low Conga","Cowbell","Vibraslap",
			"Low-Mid Tom","High Agogo","Open Hi Conga"};
	int []instruments = new int[]{35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	//actual drum keys
	
	public static void main(String[] args) {

		new HeadsFirstJukebox().buildGui();
		
	}
	
	public void buildGui() {
		setUpMidi();
		frame = new JFrame ("Cyber Beatbox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel (layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		checkBoxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		
		//SET UP BUTTONS
		JButton start = new JButton ("Start");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);
		
		JButton stop = new JButton ("Stop");
		start.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		JButton tempoUp = new JButton ("Tempo Up");
//		start.addActionListener(new MyTempoUpListener());
		buttonBox.add(tempoUp);
		
		JButton tempoDown = new JButton ("Tempo Down");
//		start.addActionListener(new MyTempoDownListener());
		buttonBox.add(tempoDown);
		
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
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkBoxList.add(c);
			mainPanel.add(c);
		}
		
		frame.setBounds(50,50,300,300);
		frame.pack();
		frame.setVisible(true);
	}

	public void setUpMidi() {
		try {
			seqr = MidiSystem.getSequencer();
			seqr.open();
			seq = new Sequence(Sequence.PPQ, 4);
			tr = seq.createTrack();
			seqr.setTempoInBPM(120);
		}catch(Exception e) {e.printStackTrace();}
	}
	//turn checkboxstate into MidiEvents
	public void buildTrackAndStart() {
		int[]trackList = null;
		//make fresh track
		if(tr!=null)
			seq.deleteTrack(tr);
		tr = seq.createTrack();
		//start outer loop
		for (int i=0;i<16;i++) {
			trackList = new int[16];
			int key = instruments[i];
			//start inner loop
			for (int j=0;j<16;j++) {
				JCheckBox jc = (JCheckBox) checkBoxList.get(j+(16*i));
				if(jc.isSelected()) {
					trackList[j]=key;
				}else {
					trackList[j]=0;
				}
			}//close inner loop
			makeTracks(trackList);
			tr.add(makeEvent(176,1,127,0,16));
		}//close outer loop
		tr.add(makeEvent(176,1,127,0,16)); //for beat 16 (loop goes 0-15)
		
		try {
			seqr.setSequence(seq);
			seqr.setLoopCount(seqr.LOOP_CONTINUOUSLY);//specify loop iterations
			seqr.start();
			seqr.setTempoInBPM(120);
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public void makeTracks(int []list) {
		for (int i=0;i<16;i++) {
			int key = list[i];
			if(key!=0) {
				tr.add(makeEvent(144,9,key,100,i));
				tr.add(makeEvent(128,9,key,100,i+1));
			}
		}
	}
	//start inner class
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent ev = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one ,two);
			ev = new MidiEvent(a,tick);
		}catch(Exception e) {}
		return ev;
	}//close inner class
	
	//start inner class
	public class MyStartListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			buildTrackAndStart();
		}
	}//close inner class
	
	public class MyStopListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
//			seqr.stop();
		}
	}//close inner class
	public class MyTempoUpListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = seqr.getTempoFactor();
			seqr.setTempoFactor((float)(tempoFactor*1.03));
		}
	}//close inner class
	public class MyTempoDownListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = seqr.getTempoFactor();
			seqr.setTempoFactor((float)(tempoFactor*0.97));
		}
	}//close inner class
}
