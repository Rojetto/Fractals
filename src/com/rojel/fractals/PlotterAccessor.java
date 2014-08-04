package com.rojel.fractals;

import aurelienribon.tweenengine.TweenAccessor;

public class PlotterAccessor implements TweenAccessor<Plotter> {
	public static final int MIN_MAX = 1;
	public static final int CENTER_WIDTH_HEIGHT = 2;

	@Override
	public int getValues(Plotter target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case MIN_MAX:
			returnValues[0] = (float) target.getMinX();
			returnValues[1] = (float) target.getMinY();
			returnValues[2] = (float) target.getMaxX();
			returnValues[3] = (float) target.getMaxY();
			return 4;
		case CENTER_WIDTH_HEIGHT:
			returnValues[0] = (float) target.getCenterX();
			returnValues[1] = (float) target.getCenterY();
			returnValues[2] = (float) target.getWidth();
			returnValues[3] = (float) target.getHeight();
			return 4;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Plotter target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case MIN_MAX:
			target.setMinX(newValues[0]);
			target.setMinY(newValues[1]);
			target.setMaxX(newValues[2]);
			target.setMaxY(newValues[3]);
			break;
		case CENTER_WIDTH_HEIGHT:
			target.setCenterX(newValues[0]);
			target.setCenterY(newValues[1]);
			target.setWidth(newValues[2]);
			target.setHeight(newValues[3]);
			break;
		default:
			assert false;
			break;
		}
	}

}
