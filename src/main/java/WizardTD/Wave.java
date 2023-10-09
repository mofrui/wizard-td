package WizardTD;

public class Wave {

	private int waveNumber;
	private double duration;
	private double preWavePause;
	// private List<Monster> monsterList;

	private boolean started;

	// these attributes are used for countdown timers
	private double remainingTime;
	private double remainingPause;
	
	public Wave(int waveNumber, double duration, double preWavePause) {
		this.waveNumber = waveNumber;
		this.duration = duration;
		this.preWavePause = preWavePause;

		if (waveNumber == 1) {
			started = true; // the wave 1 will immediately start
		} else {
			started = false;
		}
		remainingTime = duration;
		remainingPause = preWavePause;
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

	public void setStarted(boolean started) {
		this.started = started;
	}

	public void setRemainingTime(double time) {
		this.remainingTime = time;
	}

	public void setRemainingPause(double pause) {
		this.remainingPause = pause;
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean hasFinishedSpawning() {
		return (remainingTime <= 0);
	}

	public boolean hasFinishedPausing() {
		return (remainingPause <= 0);
	}
}