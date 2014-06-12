package data;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class GUIInfo {

	private JLabel jlcircleDP, jlcircleDist, jlcirclePar1, jlcirclePar2, jlcircleMinRadius, jlcircleMaxRadius, jlroboDP, jlroboMinDist, jlroboPar1, jlroboPar2, jlroboMin, jlroboMax, jlAngle, jlPoV,
	lbltxt;
	private JTextArea txtArea1;
	private Float turnAngle, ballAngle, roboAngle, PoV, lengthMultiply, close, minLength; 
	
	public Float getMinLength() {
		return minLength;
	}
	public void setMinLength(Float minLength) {
		this.minLength = minLength;
	}
	public JLabel getJlcircleDP() {
		return jlcircleDP;
	}
	public int getIntJlcircleDP() {
		return Integer.parseInt(jlcircleDP.getText());
	}
	public void setJlcircleDP(JLabel jlcircleDP) {
		this.jlcircleDP = jlcircleDP;
	}
	public JLabel getJlcircleDist() {
		return jlcircleDist;
	}
	public int getIntJlcircleDist() {
		return Integer.parseInt(jlcircleDist.getText());
	}
	public void setJlcircleDist(JLabel jlcircleDist) {
		this.jlcircleDist = jlcircleDist;
	}
	public JLabel getJlcirclePar1() {
		return jlcirclePar1;
	}
	public int getIntJlcirclePar1() {
		return Integer.parseInt(jlcirclePar1.getText());
	}
	public void setJlcirclePar1(JLabel jlcirclePar1) {
		this.jlcirclePar1 = jlcirclePar1;
	}
	public JLabel getJlcirclePar2() {
		return jlcirclePar2;
	}
	public int getIntJlcirclePar2() {
		return Integer.parseInt(jlcirclePar2.getText());
	}
	public void setJlcirclePar2(JLabel jlcirclePar2) {
		this.jlcirclePar2 = jlcirclePar2;
	}
	public JLabel getJlcircleMinRadius() {
		return jlcircleMinRadius;
	}
	public int getIntJlcircleMinRadius() {
		return Integer.parseInt(jlcircleMinRadius.getText());
	}
	public void setJlcircleMinRadius(JLabel jlcircleMinRadius) {
		this.jlcircleMinRadius = jlcircleMinRadius;
	}
	public JLabel getJlcircleMaxRadius() {
		return jlcircleMaxRadius;
	}
	public int getIntJlcircleMaxRadius() {
		return Integer.parseInt(jlcircleMaxRadius.getText());
	}
	public void setJlcircleMaxRadius(JLabel jlcircleMaxRadius) {
		this.jlcircleMaxRadius = jlcircleMaxRadius;
	}
	public JLabel getJlroboDP() {
		return jlroboDP;
	}
	public int getIntJlroboDP() {
		return Integer.parseInt(jlroboDP.getText());
	}
	public void setJlroboDP(JLabel jlroboDP) {
		this.jlroboDP = jlroboDP;
	}
	public JLabel getJlroboMinDist() {
		return jlroboMinDist;
	}
	public int getIntJlroboMinDist() {
		return Integer.parseInt(jlroboMinDist.getText());
	}
	public void setJlroboMinDist(JLabel jlroboMinDist) {
		this.jlroboMinDist = jlroboMinDist;
	}
	public JLabel getJlroboPar1() {
		return jlroboPar1;
	}
	public int getIntJlroboPar1() {
		return Integer.parseInt(jlroboPar1.getText());
	}
	public void setJlroboPar1(JLabel jlroboPar1) {
		this.jlroboPar1 = jlroboPar1;
	}
	public JLabel getJlroboPar2() {
		return jlroboPar2;
	}
	public int getIntJlroboPar2() {
		return Integer.parseInt(jlroboPar2.getText());
	}
	public void setJlroboPar2(JLabel jlroboPar2) {
		this.jlroboPar2 = jlroboPar2;
	}
	public JLabel getJlroboMin() {
		return jlroboMin;
	}
	public int getIntJlroboMin() {
		return Integer.parseInt(jlroboMin.getText());
	}
	
	public void setJlroboMin(JLabel jlroboMin) {
		this.jlroboMin = jlroboMin;
	}
	public JLabel getJlroboMax() {
		return jlroboMax;
	}
	public int getIntJlroboMax() {
		return Integer.parseInt(jlroboMax.getText());
	}
	public void setJlroboMax(JLabel jlroboMax) {
		this.jlroboMax = jlroboMax;
	}
	public JLabel getJlAngle() {
		return jlAngle;
	}
	public void setJlAngle(JLabel jlAngle) {
		this.jlAngle = jlAngle;
	}
	
	public Float getlengthMultiply () {
		return lengthMultiply;
	}
	public void setlengthMultiply (Float lengthMultiply) {
		this.lengthMultiply = lengthMultiply;
	}
	public Float getclose() {
		return close;
	}
	public void setclose(Float close) {
		this.close = close;
	}
	public JLabel getJlPoV() {
		return jlPoV;
	}
	public Float getPoV () {
		return Float.parseFloat(jlPoV.getText());
	}
	public void setJlPoV(JLabel jlPoV) {
		this.jlPoV = jlPoV;
	}
	public JLabel getLbltxt() {
		return lbltxt;
	}
	public void setLbltxt(JLabel lbltxt) {
		this.lbltxt = lbltxt;
	}
	public Float getBallAngle() {
		return ballAngle;
	}
	public void setBallAngle(Float ballAngle) {
		this.ballAngle = ballAngle;
	}
	public Float getRoboAngle() {
		return roboAngle;
	}
	public void setRoboAngle(Float roboAngle) {
		this.roboAngle = roboAngle;
	}
	public Float getTurnAngle() {
		return turnAngle;
	}
	public void setTurnAngle(Float turnAngle) {
		this.turnAngle = turnAngle;
	}
	public JTextArea getTxtArea1() {
		return txtArea1;
	}
	public void setTxtArea1(JTextArea txtArea1) {
		this.txtArea1 = txtArea1;
	}

	
}
