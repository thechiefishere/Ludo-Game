package com.jbaba.ludo.abstractclasses;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.Serializable;

public abstract class AbsSprite implements Serializable
{
    private boolean visible;
    private boolean active;
    private boolean priority;

    public abstract void initSprite();
    public abstract void updateSprite();
    public abstract void drawSprite(Canvas canvas, Paint paint);

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }
}
