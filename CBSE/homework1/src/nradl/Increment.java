package nradl;

import java.math.BigDecimal;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

@Component(provides = @Interface(name = "res", signature = nradl.Number.class))
public class Increment implements Number {

	@Requires(name = "a")
	private Number a;

	public Object[] getNumber() {
		Object[] left = a.getNumber();
		BigDecimal leftNr = (BigDecimal) left[0];
		String leftStr = (String) left[1];
		return new Object[] { leftNr.add(BigDecimal.ONE), "(" + leftStr + "++)" };
	}
}