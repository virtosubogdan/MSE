package nradl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;
@Component(provides = @Interface(name = "res", signature = nradl.Number.class))
public class Divider implements Number {

	@Requires(name = "divA")
	private Number a;
	@Requires(name = "divB")
	private Number b;

	public Object[] getNumber() {
		Object[] left = a.getNumber();
		Object[] right = b.getNumber();
		BigDecimal leftNr = (BigDecimal) left[0];
		BigDecimal rightNr = (BigDecimal) right[0];
		String leftStr = (String) left[1];
		String rightStr = (String) right[1];
		if(rightNr.equals(BigDecimal.ZERO)){
			rightNr=BigDecimal.ONE;
			rightStr+="//1";
		}
		return new Object[] { leftNr.divide(rightNr, 2, RoundingMode.HALF_UP),
				"(" + leftStr + "/" + rightStr + ")" };
	}
}