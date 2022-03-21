import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.*;

import javafx.scene.layout.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
public class MiniPlayer{
	JButton colorBtn, txtBtn;
	JLabel justALabel;
	int x=20,y=20;
	static JFrame frame = new JFrame("First MiniPlayer!");;
	static MyDrawPanel myDrawPanel;
	
//	public static void main(String[] args) {
//
//		new MiniPlayer().go();
//	}
	
	public void setGui() {
		myDrawPanel = new MyDrawPanel();
		JPanel panel = new JPanel();
		panel.setBackground(Color.gray);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setSize(400, 300);
		
		colorBtn = new JButton("Repaint");
//		colorBtn.addActionListener(new ColorListener());

		txtBtn = new JButton("Text");
		txtBtn.addActionListener(new LabelListener());
		justALabel = new JLabel ("Nothing here yet");
		panel.add(txtBtn);
		panel.add(justALabel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//program stops as soon as the window is closed
//		frame.setContentPane(myDrawPanel);
		frame.getContentPane().add(BorderLayout.CENTER, myDrawPanel);

		frame.getContentPane().add(BorderLayout.EAST, panel);
		frame.setBounds(30, 30, 300, 300);
		frame.setVisible(true);
		
	}
	
	public void go() {
		setGui();
		try {
			//reminder, you dont create a new sequencer, you request it
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			//using this, you can trigger an event during every note played
			sequencer.addControllerEventListener(myDrawPanel, new int[] {127});
//			sequencer.addControllerEventListener(this, eventsIWant);
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			int r=0;
			for (int i=0; i<60; i+=4) {
				r=(int)((Math.random()*50)+1);
				track.add(makeEvent(144,1,r,74,i));
				track.add(makeEvent(176,1,127,0,i));//this is the trigger
				track.add(makeEvent(128,1,r,105,i+2));
				
			}
			//start it running
			sequencer.setSequence(seq);
			sequencer.setTempoInBPM(220);
			sequencer.start();
//			for (int i=0;i<20;i++) {
//				myDrawPanel.repaint();
//				Thread.sleep(300);
//			}
			
			
		}catch (Exception e	) {e.printStackTrace();}
	}
	public static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent ev = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one ,two);
			ev = new MidiEvent(a,tick);
		}catch(Exception e) {}
		return ev;
	}
	
	class MyDrawPanel extends JPanel implements ControllerEventListener{
		boolean state = false;
		//this is what trigger 127 triggers
		@Override
		public void controlChange(ShortMessage event) {
			state=true;
			repaint();
//			System.out.println("la");
		}
		@Override
		public void paintComponent (Graphics g) {
			if(state) {
				//extend graphics class to use gradient paint 
			Graphics2D g2d = (Graphics2D) g;
			GradientPaint gradient = new GradientPaint(70, 70, 
					Color.getHSBColor((float) Math.random(), (float) Math.random(), (float) Math.random()), 150,150, 
					Color.getHSBColor((float) Math.random(), (float) Math.random(), (float) Math.random()));
//			g.setColor(Color.orange);
			g2d.setPaint(gradient);
			g.fillRect((int) (Math.random()*40)+10, 
					(int) (Math.random()*40)+10, 
					(int) (Math.random()*120)+10, 
					(int) (Math.random()*120)+10);
			state=false;
			}		
		}	
	}

	class LabelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			justALabel.setText(String.valueOf((int)(Math.random()*1234)));
			}	
	}
	class ColorListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
				frame.repaint();
			
			}
	}
}
