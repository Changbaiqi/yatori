package com.cbq.yatori.core.aggregate;

public abstract class AbstractAction {
    private AbstractAction nextAction;

    public void setNextAction(AbstractAction nextAction)
    {
        this.nextAction = nextAction;
    }

    public abstract void execute();
}
