import java.util.List;

public class ValueGetter {

	private String value;
	private List<String> ls;
	private boolean bool;
	private int inti;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setLValue(List<String> ls) {
		this.ls = ls;
	}

	public List<String> getLValue() {
		return ls;
	}

	public void setBoolean(boolean bool) {
		this.bool = bool;
	}

	public boolean getBoolean() {
		return bool;
	}

	public void setInt(int inti) {
		this.inti = inti;
	}

	public int getInt() {
		return inti;
	}
}
