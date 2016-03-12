package net.ilexiconn.pixle.event.bus;

import net.ilexiconn.pixle.crash.CrashReport;
import net.ilexiconn.pixle.event.Event;

import java.lang.reflect.Method;

public class EventMethod {
    private Object parent;
    private Method method;
    private Class<?> eventClass;

    public EventMethod(Object parent, Method method, Class<?> eventClass) {
        this.parent = parent;
        this.method = method;
        this.eventClass = eventClass;
    }

    public boolean canHandleEvent(Event event) {
        return event.getClass() == eventClass;
    }

    public void invoke(Event event) {
        if (event.getClass() != eventClass) {
            throw new RuntimeException("Method called with wrong event! " + this);
        }
        try {
            method.invoke(parent, event);
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, "Failed calling event method"));
        }
    }
}
