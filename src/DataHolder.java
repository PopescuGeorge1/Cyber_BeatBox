
public class DataHolder {
	private String []instrumentNames = new String[]{"Bass Drum","Closed Hi-Hat",
			"Open Hi-Hat","Acoustic Snare","Crash Cymbal",
			"Hand Clap","High Tom", "High Bongo","Maracas",
			"Whistle","Low Conga","Cowbell","Vibraslap",
			"Low-Mid Tom","High Agogo","Open Hi Conga"};
	private int []instruments = new int[]{35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	//actual drum keys
	public DataHolder() {
		
	}
	
	public String[] getInstrumentName() {
		return instrumentNames;
	}
	public int[] getInstrumentNr() {
		return instruments;
	}
}
