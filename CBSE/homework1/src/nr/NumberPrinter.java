package nr;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

public class NumberPrinter implements Runnable, BindingController {

	private Number nr;

	public void run() {
		Object[] result = nr.getNumber();
		System.out.println("Operation: " + result[1]);
		System.out.println("Result: " + result[0]);
	}

	@Override
	public void bindFc(String arg0, Object arg1)
			throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (arg0.equals("n")) {
			nr = (Number) arg1;
		}
	}

	@Override
	public String[] listFc() {
		return new String[] { "n" };
	}

	@Override
	public Object lookupFc(String arg0) throws NoSuchInterfaceException {
		if (arg0.equals("n")) {
			return nr;
		}
		return null;
	}

	@Override
	public void unbindFc(String arg0) throws NoSuchInterfaceException,
			IllegalBindingException, IllegalLifeCycleException {
		if (arg0.equals("n")) {
			nr = null;
		}
	}
}