package sandbox;
import java.util.Map;
public class TouchscreenVariable {

	private String name;
	private String value;
	private String maxBits;

	//constructors
	public TouchscreenVariable() {
	}
	public TouchscreenVariable(String name, String value) {
		this.name = name;
		this.value = value;
	}
	//getter
	public String getName() {
		return name;
	}
	//setter
	public void setName(String name) {
		this.name = name;
	}
	//getter
	public String getValue() {
		return value;
	}
	//setter
	public void setValue(String value) {
		this.value = value;
	}
//getter
	public String getMaxBits() {
		return maxBits;
	}
	//setter
	public void setMaxBits(String maxBits) {
		this.maxBits = maxBits;
	}
	@Override
	public String toString() {
		return String.format("TouchscreenVariable{name=%s, value=%s}", getName(), getValue());
	}
	 
}