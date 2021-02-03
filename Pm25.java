import java.util.Date;

public class Pm25 {
	private int siteid;
	private String sitename;
	private String county;
	private Date monitordate;
	private int concentration;

	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Date getMonitordate() {
		return monitordate;
	}

	public void setMonitordate(Date monitordate) {
		this.monitordate = monitordate;
	}

	public int getConcentration() {
		return concentration;
	}

	public void setConcentration(int concentration) {
		this.concentration = concentration;
	}

	@Override
	public String toString() {
		return "Pm25 [siteid=" + siteid + ", sitename=" + sitename + ", county=" + county + ", monitordate="
				+ monitordate + ", concentration=" + concentration + "]";
	}
	
	
	
}
