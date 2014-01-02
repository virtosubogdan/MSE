package nradl;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

@Component(provides = @Interface(name = "r", signature = java.lang.Runnable.class))
public class NumberPrinter implements Runnable {

	@Requires(name = "n")
	private Number nr;

	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Object[] result = nr.getNumber();
				System.out.println("Operation: " + result[1]);
				System.out.println("Result: " + result[0]);
				synchronized (this) {
					wait(1500);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
}