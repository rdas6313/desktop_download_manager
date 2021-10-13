package com.rdas6313;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Observable {
    
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void attach(PropertyChangeListener listener){
        if(support == null)
            return;
        support.addPropertyChangeListener(listener);
    }

    public void detach(PropertyChangeListener listener){
        if(support == null)
            return;
        support.removePropertyChangeListener(listener);
    }

    public void notifyObservers(String propertyName,Object oldValue,Object newValue){
        if(support == null)
            return;
        support.firePropertyChange(propertyName, oldValue, newValue);
    }
}
