package se.embargo.core.widget;

public interface INumberPicker {
    /**
     * Interface used to format the number into a string for presentation
     */
    public interface Formatter {
        String format(int value);
    }

    public void setMinValue(int start);
    public void setMaxValue(int end);
	public void setFormatter(Formatter formatter);
	public void setValue(int value);
	public int getValue();
}
