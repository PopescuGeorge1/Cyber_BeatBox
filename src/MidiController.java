import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.midi.*;
import javax.swing.JCheckBox;

public class MidiController implements ControllerEventListener{
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	ArrayList <JCheckBox> checkBoxList;
	
	public MidiController (ArrayList <JCheckBox> c) {
		checkBoxList = c;
		setUpMidi();
		buildTrackAndStart();
	}
	
	public MidiEvent makeEvent (int comd, int chan, int one, int two, int tick) {
		MidiEvent midiEvent = null;
		try {
			ShortMessage shortMsg = new ShortMessage();
			shortMsg.setMessage(comd, chan, one ,two);
			midiEvent = new MidiEvent(shortMsg,tick);
		}catch(Exception e) {}
		return midiEvent;
	}

	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		}catch(Exception e) {e.printStackTrace();}
	}
	//turn checkboxstate into MidiEvents
	public void buildTrackAndStart() {
		int[]trackList = null;
		int []instrumentNr = new DataHolder().getInstrumentNr();
		//make fresh track
		if(track!=null)
			sequence.deleteTrack(track);
		track = sequence.createTrack();
		//start outer loop
		for (int i=0;i<16;i++) {
			trackList = new int[16];
			int key = instrumentNr[i];
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
			track.add(makeEvent(176,1,127,0,16));
		}//close outer loop
		track.add(makeEvent(176,1,127,0,16)); //for beat 16 (loop goes 0-15)
		
		try {
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);//specify loop iterations
			sequencer.start();
			sequencer.setTempoInBPM(120);
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public void makeTracks(int []list) {
		
		
		for (int i=0;i<16;i++) {
			int key = list[i];
			if(key!=0) {
				track.add(makeEvent(144,9,key,100,i));
				track.add(makeEvent(128,9,key,100,i+1));
			}
		}
	}

		public void start() {
			buildTrackAndStart();
		}

		public void stop() {
			sequencer.stop();
		}

		public void tempoUp() {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor*1.5));
		}

		public void tempoDown() {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor*0.5));
		}

	@Override
	public void controlChange(ShortMessage event) {
		//does something every time a note is played, good for animations during playback
		
	}


}
