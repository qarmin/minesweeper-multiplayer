package server;

public class Timer implements Runnable{

	public float currentTime = 0;
	public boolean active = false;
	@Override
	
	synchronized public void run() {
		while(true) {
				if(active) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					currentTime += 0.1f;
				}
			}
		}

	synchronized public void start() {
		active = true;
	}
	synchronized public void stop() {
		active = false;
	}
	
	synchronized public float GetTime() {
		return currentTime;
	}
	
}
