package utils;

/**
 * @author dinghao
 * @version 1.0.0
 * @ClassName TimeRecorder.java
 * @Description TODO
 * @createTime 2019年08月20日 09:31:00
 */
public class TimeRecorder {

    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public long getDuration() {
        endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        return duration;
    }
}
