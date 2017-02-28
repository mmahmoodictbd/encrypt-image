import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class LFSR {

	private String sid;

	public LFSR(String sid) {
		this.sid = sid;
	}

	public long next() {

		int[] taps = getTaps(this.sid.length());

		StringBuffer tempSequence = new StringBuffer(this.sid);
		StringBuffer tempSequence1;

		boolean tapIn1, tapIn2, tapOut;

		tempSequence1 = leftShift(tempSequence);
		for (int tap : taps) {
			tapIn1 = tempSequence.charAt(this.sid.length() - tap + 1) == '1' ? true : false;
			tapIn2 = tempSequence.charAt(0) == '1' ? true : false;
			tapOut = tapIn1 ^ tapIn2;
			tempSequence1.setCharAt(this.sid.length() - tap, tapOut ? '1' : '0');
		}
		tempSequence = tempSequence1;
		this.sid = tempSequence.toString();

		return Long.valueOf(tempSequence.toString(), 2);

	}

	private StringBuffer leftShift(StringBuffer sequence) {
		StringBuffer sb = new StringBuffer();
		sb.append(sequence.substring(1, sequence.length()));
		sb.append(sequence.charAt(0));
		return sb;
	}

	private int[] getTaps(int degree) {

		int[] taps = null;

		switch (degree) {
		case 5:
			taps = new int[] { 3 };
			break;
		case 7:
			taps = new int[] { 6 };
			break;
		case 8:
			taps = new int[] { 6, 5, 4 };
			break;
		case 32:
			taps = new int[] { 22, 2 };
			break;
		}

		return taps;
	}

	public List<String> getPseudoRandomSequencesList(int max) {
		List<String> pseudoRandomSequencesList = new ArrayList<String>();
		for (int i = 0; i < max; i++) {
			pseudoRandomSequencesList.add(Long.toString(next()));
		}
		return pseudoRandomSequencesList;
	}

	public void writePseudoRandomSequencesList(List<String> pseudoRandomSequencesList, String fileName) {
		System.out.println(pseudoRandomSequencesList);

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(new File(fileName));
			bufferedWriter = new BufferedWriter(fileWriter);
			for (String num : pseudoRandomSequencesList) {
				bufferedWriter.write(num + "\n");
			}
			bufferedWriter.close();

		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				fileWriter.close();
				bufferedWriter.close();
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}

	}

}