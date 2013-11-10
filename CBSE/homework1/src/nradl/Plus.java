package nradl;

import java.math.BigDecimal;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

@Component(provides = @Interface(name = "res", signature = nradl.Number.class))
public class Plus implements Number {

	@Requires(name = "plusA")
	private Number a;
	@Requires(name = "plusB")
	private Number b;

	public Object[] getNumber() {
		Object[] left = a.getNumber();
		Object[] right = b.getNumber();
		BigDecimal leftNr = (BigDecimal) left[0];
		BigDecimal rightNr = (BigDecimal) right[0];
		String leftStr = (String) left[1];
		String rightStr = (String) right[1];
		return new Object[] { leftNr.add(rightNr),
				"(" + leftStr + "+" + rightStr + ")" };
	}
}