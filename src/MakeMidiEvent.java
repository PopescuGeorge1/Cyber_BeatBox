import javax.sound.midi.ShortMessage;
import javax.sound.midi.*;

public class MakeMidiEvent implements ControllerEventListener{
	public static void main(String[] args) {
		try {
			//reminder, you dont create a new sequencer, you request it
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			
			int [] eventsIWant = {127};
//			sequencer.addControllerEventListener(this, eventsIWant);
			//make a sequence and a track
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			for (int i=5; i<61; i+=4) {
				track.add(makeEvent(144,1,i,100,i));
				track.add(makeEvent(128,1,i,100,i+2));
					
			}
			//start it running
			sequencer.setSequence(seq);
			sequencer.setTempoInBPM(220);
			sequencer.start();
			
		}catch (Exception e) {}
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
	@Override
	public void controlChange(ShortMessage event) {
		System.out.println("la");
	}
}
