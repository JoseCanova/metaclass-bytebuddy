package metaclass.bytebuddy;

import java.util.Optional;

public class SimplePojo {

	private String attribute;
	
	public SimplePojo() {
	}
	
	public String accessor() {
		return attribute;
	}
	
	public Optional<?> mutator(String value) {
		this.attribute=value;
		return Optional.of(attribute);
	}

}
