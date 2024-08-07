package tools;

public class Utils {
	
	/**
	 * Pauses the current thread for a set period of time.
	 * 
	 * @param time the time to pause, in milliseconds.
	 */
	public static void pause(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// We are happy with interruptions, so do not report any exceptions.
			e.printStackTrace();
		}
	}
}
