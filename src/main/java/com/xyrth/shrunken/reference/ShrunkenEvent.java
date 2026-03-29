package com.xyrth.shrunken.reference;

import com.xyrth.shrunken.event.GenericSpawnEvent;

// This class provides a list of event types, with properties for their description and corresponding class
public enum ShrunkenEvent {

    UNKNOWN("Unknown Event", GenericSpawnEvent.class);

    public final String eventAction;
    public final Class<? extends GenericSpawnEvent> genericEventClass;

    ShrunkenEvent(String eventAction, Class<? extends GenericSpawnEvent> genericEventClass) {
        this.eventAction = eventAction;
        this.genericEventClass = genericEventClass;
    }

    /**
     * Checks if the specified name is a valid enum for the class.
     *
     * @param enumName the enum name, null returns false
     * @return true if the enum name is valid, otherwise false
     */
    public static <E extends Enum<E>> boolean isValidEnum(final String enumName) {
        if (enumName == null) {
            return false;
        }
        try {
            Enum.valueOf(ShrunkenEvent.class, enumName);
            return true;
        } catch (final IllegalArgumentException ex) {
            return false;
        }
    }
}
