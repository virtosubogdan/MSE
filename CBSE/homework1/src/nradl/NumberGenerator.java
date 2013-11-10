package nradl;

import java.math.BigDecimal;
import java.util.Random;

import org.objectweb.fractal.fraclet.annotations.Attribute;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

@Component(provides = @Interface(name = "res", signature = nradl.Number.class))
public class NumberGenerator implements Number {

	private Random gen;
	@Attribute(value = "10")
	private String limit="10";

	public NumberGenerator() {
		gen = new Random();
	}

	public Object[] getNumber() {
		int l = Integer.valueOf(limit);
		int nr = gen.nextInt(l);
		return new Object[] { new BigDecimal(nr), "" + nr };
	}

}