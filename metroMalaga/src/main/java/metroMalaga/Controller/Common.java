package metroMalaga.Controller;

import java.text.DecimalFormat;

public class Common {
	public static String formatSize(long bytes) {
		if (bytes <= 0) {
			return "0 B";
		}
		final long K = 1024;
		final long M = K * 1024;
		final long G = M * 1024;

		DecimalFormat df = new DecimalFormat("0.0");

		if (bytes < K) {
			return bytes + " B"; 
		} else if (bytes < M) {
			return df.format((double) bytes / K) + " KB"; 
		} else if (bytes < G) {
			return df.format((double) bytes / M) + " MB"; 
		} else {
			return df.format((double) bytes / G) + " GB";
		}
	}
}
