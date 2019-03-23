package com.vlad;

import com.sbix.jnotify.NPosition;
import com.sbix.jnotify.NoticeType;
import com.sbix.jnotify.NoticeWindow;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class output notification about com.vlad.Tasks on defined interval.
 */
public class Notification {
    /**
     * Constrictor is looking for tasks every 5 minutes and outputs notifications.
     * @param linkedTaskListCh
     */
    Notification(LinkedTaskList linkedTaskListCh) {
        final Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LinkedTaskList linkedTaskList = (LinkedTaskList)Tasks.incoming(linkedTaskListCh, new Date(), new Date(new Date().getTime() + 5 * 60000));
                for (Task task: linkedTaskList) {
                    new NoticeWindow(NoticeType.WARNING_NOTIFICATION, "In the nearest time you have task: "
                            + task.getTitle(), NoticeWindow.LONG_DELAY, NPosition.BOTTOM_RIGHT);
                }
            }
        }, 0, 5*60*1000);
    }
}
