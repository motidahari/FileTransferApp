
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
	public static void main(String[] args) {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		Folders folders = new Folders();
		//folders.run();
		scheduledExecutorService.scheduleAtFixedRate(folders, 0, 1, TimeUnit.HOURS);
	}

}
