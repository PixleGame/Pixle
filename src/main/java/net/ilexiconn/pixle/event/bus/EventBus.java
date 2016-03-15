package net.ilexiconn.pixle.event.bus;

import com.esotericsoftware.minlog.Log;
import net.ilexiconn.pixle.event.Event;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private List<EventMethod> eventMethodList = new ArrayList<>();

    public void register(Object object) {
        Log.info("EventBus", "Attempting to register object " + object);
        if (object != null) {
            for (Method method : object.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventHandler.class) && method.getParameters().length == 1) {
                    Log.info("EventBus", "Found potential event method " + method);
                    Parameter parameter = method.getParameters()[0];
                    if (Event.class.isAssignableFrom(parameter.getType())) {
                        eventMethodList.add(new EventMethod(object, method, parameter.getType()));
                    }
                }
            }
        }
    }

    public boolean post(Event event) {
        eventMethodList.stream().filter(eventMethod -> eventMethod.canHandleEvent(event)).forEach(eventMethod -> eventMethod.invoke(event));
        return !event.isCanceled();
    }
}
