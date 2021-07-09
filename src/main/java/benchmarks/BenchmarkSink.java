package benchmarks;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * Sink per la raccolta delle metriche di benchmark
 */
public class BenchmarkSink implements SinkFunction<String> {

	@Override
	public void invoke(String value, Context context) {
		SynchroPerformances.incrementCounter();
	}
}
