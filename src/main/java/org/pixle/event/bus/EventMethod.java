package org.pixle.event.bus;

import org.pixle.event.Event;
import org.pixle.util.CrashReport;

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
        return this.eventClass == event.getClass();
    }

    public void invoke(Event event) {
        if (event.getClass() != this.eventClass) {
            throw new RuntimeException("Method called with wrong event! " + this);
        }
        try {
            this.method.invoke(this.parent, event);
        } catch (Exception e) {
            System.err.println(CrashReport.makeCrashReport(e, "Failed calling event method"));
        }
    }
}
