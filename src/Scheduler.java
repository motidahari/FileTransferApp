
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//        OldRun folders = new OldRun();
//        folders.run();
        runApp runApp = new runApp();
		runApp.run();
//        scheduledExecutorService.scheduleAtFixedRate(runApp, 0, 1, TimeUnit.HOURS);
    }

}
