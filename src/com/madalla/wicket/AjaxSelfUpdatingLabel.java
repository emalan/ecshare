package com.madalla.wicket;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;

public class AjaxSelfUpdatingLabel extends Label{

    private static final long serialVersionUID = 1L;
    
    private int duration;
    private AjaxSelfUpdatingTimerBehavior timer;

    public AjaxSelfUpdatingLabel(String componentId, IModel model, int duration){
        super(componentId, model);
        this.duration = duration;
    }

    public void startTimer() {
        if (timer == null){
            timer = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(duration));
            this.add(timer);
        }
    }

}
