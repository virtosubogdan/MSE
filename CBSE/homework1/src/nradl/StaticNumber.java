package nradl;

import java.math.BigDecimal;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

@Component(provides = @Interface(name = "res", signature = nradl.Number.class))
public class StaticNumber implements Number {

	private int number = 10;

	public StaticNumber() {
	}

	public Object[] getNumber() {
		return new Object[] { new BigDecimal(number), "" + number };
	}

}