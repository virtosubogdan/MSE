package nradl;

import java.math.BigDecimal;
import java.util.Random;

import org.objectweb.fractal.fraclet.annotations.Attribute;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

@Component(provides = @Interface(name = "res", signature = nradl.Number.class))
public class StaticNumberGenerator implements Number {

	private Random gen;
	@Attribute(value = "100")
	private String limit = "100";
	private int number;

	public StaticNumberGenerator() {
		gen = new Random();
		number = gen.nextInt(Integer.valueOf(limit));
	}

	public Object[] getNumber() {
		return new Object[] { new BigDecimal(number), "" + number };
	}

}