package benchmarks;

public class SynchroPerformances {

	// contatore delle tuple
	private static long counter = 0L;
	// tempo del primo evento
	private static long startTime;

	/**
	 * Aggiornato ad ogni nuovo evento raccolto dal sink
	 */
	public static synchronized void incrementCounter() {
		if (counter == 0L) {
			startTime = System.currentTimeMillis();
			System.out.println("Initialized!");
		}
		counter++;
		double currentTime = System.currentTimeMillis() - startTime;

		// stampa delle metriche raccolte
		System.out.println("Mean throughput: " + (counter/currentTime) + "\n" + "Mean latency: " +
				(currentTime/counter));
	}
}
