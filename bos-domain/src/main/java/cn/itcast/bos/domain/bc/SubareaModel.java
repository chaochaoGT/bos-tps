package cn.itcast.bos.domain.bc;
// Generated 2017-7-18 12:28:45 by Hibernate Tools 3.2.2.GA

import javax.persistence.Transient;

public class SubareaModel implements java.io.Serializable {

	// spring data jpa 开发效率高
	private String id;
	private DecidedZone decidedZone;
	private Region region;
	private String addresskey;
	private String startnum;
	private String endnum;
	private Character single;
	private String position;

	@Transient
	public String getSid() {
		return this.id;
	}

	public SubareaModel() {
	}

	public SubareaModel(DecidedZone decidedZone, Region region, String addresskey, String startnum, String endnum, Character single, String position) {
		this.decidedZone = decidedZone;
		this.region = region;
		this.addresskey = addresskey;
		this.startnum = startnum;
		this.endnum = endnum;
		this.single = single;
		this.position = position;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DecidedZone getDecidedZone() {
		return this.decidedZone;
	}

	public void setDecidedZone(DecidedZone decidedZone) {
		this.decidedZone = decidedZone;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getAddresskey() {
		return this.addresskey;
	}

	public void setAddresskey(String addresskey) {
		this.addresskey = addresskey;
	}

	public String getStartnum() {
		return this.startnum;
	}

	public void setStartnum(String startnum) {
		this.startnum = startnum;
	}

	public String getEndnum() {
		return this.endnum;
	}

	public void setEndnum(String endnum) {
		this.endnum = endnum;
	}

	public Character getSingle() {
		return this.single;
	}

	public void setSingle(Character single) {
		this.single = single;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
