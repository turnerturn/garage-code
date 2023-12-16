package sandbox;
import java.util.Map;
public class TouchscreenMemoryMessage {

	private String variableName;
	private String variableValue;
	private Map<String,Object> tags;
	public TouchscreenMemoryMessage() {
	}

	public TouchscreenMemoryMessage( String variableName, String variableValue,Map<String,Object> tags) {
	
		this.variableName = variableName;
		this.variableValue = variableValue;
		this.tags = tags;
	}


	public String getName() {
		return variableName;
	}

	public void setName(String variableName) {
		this.variableName = variableName;
	}

	public String getValue() {
		return variableValue;
	}

	public void setValue(String variableValue) {
		this.variableValue = variableValue;
	}

	public Map<String,Object> getTags() {
		return tags;
	}

	public void setTags(Map<String,Object> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return String.format("TouchscreenMemoryMessage{variableName=%s, variableValue=%s, tags=%s}", getName(), getValue(),getTags());
	}

}
