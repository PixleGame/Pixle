package net.ilexiconn.pixle.event;

public class Event {
    private boolean canceled = false;

    public boolean isCancelable() {
        return getClass().isAnnotationPresent(Cancelable.class);
    }

    public void setCanceled() {
        if (!isCancelable()) {
            throw new RuntimeException("Event not cancelable: " + this);
        } else {
            canceled = true;
        }
    }

    public boolean isCanceled() {
        return canceled;
    }
}
