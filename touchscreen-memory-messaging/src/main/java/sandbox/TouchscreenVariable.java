package sandbox;

public class TouchscreenVariable {

	private String name;
	private String value;
	private String dataType;
	private String ioAddress;
	private Integer maxCount;
public TouchscreenVariable() {}
	public TouchscreenVariable(String name, String value, String dataType, String ioAddress, Integer maxCount) {
		this.name = name;
		this.value = value;
		this.dataType = dataType;
		this.ioAddress = ioAddress;
		this.maxCount = maxCount;
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
	public String getDataType() {
		return dataType;
	}
	//setter
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	//getter
	public String getIoAddress() {
		return ioAddress;
	}
	//setter
	public void setIoAddress(String ioAddress) {
		this.ioAddress = ioAddress;
	}
	//getter
	public Integer getMaxCount() {
		return maxCount;
	}
	//setter
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	//tostring
	public String toString() {
		return "TouchscreenVariable(name=" + getName() + ", value=" + getValue() + ", dataType=" + getDataType()
				+ ", ioAddress=" + getIoAddress() + ", maxCount=" + getMaxCount() + ")";
	}
}