package nradl;

import java.math.BigDecimal;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

@Component(provides = @Interface(name = "res", signature = nradl.Number.class))
public class StaticNumberGenerator implements Number, StaticNumber {

	private int number;

	public StaticNumberGenerator() {
		number = 0;
	}

	public Object[] getNumber() {
		return new Object[] { new BigDecimal(number), "" + number };
	}

	public void setValue(int value) {
		number = value;
	}

	public int getValue() {
		return number;
	}

}