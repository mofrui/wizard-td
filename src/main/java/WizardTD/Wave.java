package WizardTD;

public class Wave {

	private int waveNumber;

	private double duration;
	private double preWavePause;

	private double remainingTime;
	private double remainingPause;
	
	public Wave(int waveNumber, double duration, double preWavePause) {
		this.waveNumber = waveNumber;
		this.duration = duration;
		this.preWavePause = preWavePause;

		this.remainingTime = duration;
		this.remainingPause = preWavePause;
	}

	public int getWaveNumber() {
		return waveNumber;
	}

	public double getDuration() {
		return duration;
	}

	public double getPreWavePause() {
		return preWavePause;
	}

	public double getRemainingTime() {
		return remainingTime;
	}

	public double getRemainingPause() {
		return remainingPause;
	}

	public void setRemainingTime(double time) {
		this.remainingTime = time;
	}

	public void setRemainingPause(double pause) {
		this.remainingPause = pause;
	}

	public boolean hasFinishedSpawning() {
		return (remainingTime <= 0);
	}

	public boolean hasFinishedPausing() {
		return (remainingPause <= 0);
	}
}