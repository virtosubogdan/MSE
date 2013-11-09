package nr;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.util.Fractal;

public class ExpressionsPureJava {
	private Component boot;
	private Component root;
	private ComponentType runnableType;
	private ComponentType generatorType;
	private ComponentType printerType;
	private ComponentType operatorType;
	private ComponentType simpleOperatorType;

	public ExpressionsPureJava() throws Exception {
		createTypes();
		createAndBindComponents();
	}

	private void createTypes() throws Exception {
		boot = Fractal.getBootstrapComponent();
		TypeFactory tf = Fractal.getTypeFactory(boot);
		runnableType = tf
				.createFcType(new InterfaceType[] { tf.createFcItfType("r",
						"java.lang.Runnable", false, false, false) });
		printerType = tf.createFcType(new InterfaceType[] {
				tf.createFcItfType("r", "java.lang.Runnable", false, false,
						false),
				tf.createFcItfType("n", "nr.Number", true, false, false) });
		generatorType = tf.createFcType(new InterfaceType[] {
				tf.createFcItfType("n", "nr.Number", false, false, false),
				tf.createFcItfType("attribute-controller",
						"nr.GeneratorAttributes", false, false, false) });
		operatorType = tf.createFcType(new InterfaceType[] {
				tf.createFcItfType("a", "nr.Number", true, false, false),
				tf.createFcItfType("b", "nr.Number", true, false, false),
				tf.createFcItfType("res", "nr.Number", false, false, false) });
		simpleOperatorType = tf.createFcType(new InterfaceType[] {
				tf.createFcItfType("a", "nr.Number", true, false, false),
				tf.createFcItfType("res", "nr.Number", false, false, false) });
	}

	private void start() throws Exception {
		Fractal.getLifeCycleController(root).startFc();
		((java.lang.Runnable) root.getFcInterface("r")).run();
	}

	private void createAndBindComponents() throws Exception {
		GenericFactory cf = Fractal.getGenericFactory(boot);
		root = cf.newFcInstance(runnableType, "composite", null);
		Component printer = cf.newFcInstance(printerType, "primitive",
				"nr.NumberPrinter");
		Component generator = cf.newFcInstance(generatorType, "primitive",
				"nr.NumberGenerator");

		Component plus1 = cf
				.newFcInstance(operatorType, "primitive", "nr.Plus");
		Component mul1 = cf.newFcInstance(operatorType, "primitive",
				"nr.Multiply");
		Component plus2 = cf
				.newFcInstance(operatorType, "primitive", "nr.Plus");
		Component div1 = cf.newFcInstance(operatorType, "primitive",
				"nr.Divider");
		Component inc1 = cf.newFcInstance(simpleOperatorType, "primitive",
				"nr.Increment");

		Fractal.getContentController(root).addFcSubComponent(printer);
		Fractal.getContentController(root).addFcSubComponent(generator);
		Fractal.getContentController(root).addFcSubComponent(plus1);
		Fractal.getContentController(root).addFcSubComponent(mul1);
		Fractal.getContentController(root).addFcSubComponent(plus2);
		Fractal.getContentController(root).addFcSubComponent(div1);
		Fractal.getContentController(root).addFcSubComponent(inc1);

		Fractal.getBindingController(root).bindFc("r",
				printer.getFcInterface("r"));

		Fractal.getBindingController(printer).bindFc("n",
				plus2.getFcInterface("res"));

		Fractal.getBindingController(plus2).bindFc("a",
				mul1.getFcInterface("res"));
		Fractal.getBindingController(plus2).bindFc("b",
				inc1.getFcInterface("res"));

		Fractal.getBindingController(mul1).bindFc("a",
				plus1.getFcInterface("res"));
		Fractal.getBindingController(mul1).bindFc("b",
				generator.getFcInterface("n"));

		Fractal.getBindingController(plus1).bindFc("a",
				generator.getFcInterface("n"));
		Fractal.getBindingController(plus1).bindFc("b",
				generator.getFcInterface("n"));

		Fractal.getBindingController(inc1).bindFc("a",
				div1.getFcInterface("res"));

		Fractal.getBindingController(div1).bindFc("a",
				generator.getFcInterface("n"));
		Fractal.getBindingController(div1).bindFc("b",
				generator.getFcInterface("n"));

		GeneratorAttributes genAttr = (GeneratorAttributes) generator
				.getFcInterface("attribute-controller");
		genAttr.setLimit(10);
	}

	public static void main(String args[]) {
		try {
			ExpressionsPureJava app = new ExpressionsPureJava();
			app.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
