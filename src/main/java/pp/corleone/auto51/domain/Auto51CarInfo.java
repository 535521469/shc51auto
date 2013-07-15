package pp.corleone.auto51.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CarInfo_Auto51")
public class Auto51CarInfo implements Cloneable, Serializable {

	private static final long serialVersionUID = -4395908881660747661L;

	public static final String SOURCE_TYPE = "51auto";

	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Auto51SellerInfo.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "SellerID")
	private Auto51SellerInfo auto51SellerInfo;

	@Id
	@GeneratedValue(generator = "uuidgenerator")
	@GenericGenerator(name = "uuidgenerator", strategy = "uuid")
	private String seqID;

	@Column(name = "CityName", nullable = true)
	private String cityName;

	@Column(name = "SourceType", nullable = false)
	protected String sourceType = SOURCE_TYPE; // 58 or iautos or something else

	@Column(name = "Manufacturer", nullable = true)
	private String manufacturer; // sheng chan chang shang ,

	@Column(name = "Brand", nullable = true)
	private String brand; // suo shu pin pai

	@Column(name = "Gearbox", nullable = true)
	private String gearbox;

	@Column(name = "Displacement", nullable = true)
	private String displacement;

	@Column(name = "LicenseDate", nullable = true)
	private String licenseDate; // register date

	@Column(name = "CarColor", nullable = true)
	private String color;

	@Column(name = "RoadHaul", nullable = true)
	private String roadHaul;

	@Column(name = "StatusType", nullable = true)
	private Integer statusType;

	@Column(name = "CarSourceType", nullable = true)
	private int sellerType; // shop or person

	@Column(name = "Price", nullable = true)
	private String price;

	@Column(name = "ParkAddress", nullable = true)
	private String parkAddress; // the place where the car parks

	@Column(name = "Title", nullable = true)
	private String title;

	@Temporal(TemporalType.DATE)
	@Column(name = "DeclareDate", nullable = true)
	private Date declareDate;

	@Column(name = "SourceUrl", nullable = true)
	private String carSourceUrl;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FetchDateTime", nullable = true)
	private Date fetchDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LastActiveDateTime", nullable = true)
	private Date lastActiveDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OffLineDateTime", nullable = true)
	private Date offlineDate;

	// @Column(name = "SellerID", nullable = true)
	// private String sellerID;
	// public String getSellerID() {
	// return sellerID;
	// }
	// public void setSellerID(String sellerID) {
	// this.sellerID = sellerID;
	// }

	@Column(name = "Contacter", nullable = true)
	private String contacter;

	@Column(name = "ContacterURL", nullable = true)
	private String shopUrl;

	@Column(name = "ContacterPhone", nullable = true)
	private String contacterPhone;

	public String getParkAddress() {
		return parkAddress;
	}

	public void setParkAddress(String parkAddress) {
		this.parkAddress = parkAddress;
	}

	public String getBrand() {
		return brand;
	}

	public void setStatusType(Auto51StatusCode statusType) {
		this.setStatusType(statusType.getCode());
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public Auto51CarInfo() {
		this.setSourceType();
	}

	public Auto51SellerInfo getAuto51SellerInfo() {
		return auto51SellerInfo;
	}

	public void setAuto51SellerInfo(Auto51SellerInfo auto51SellerInfo) {
		this.auto51SellerInfo = auto51SellerInfo;
	}

	public void setSourceType() {
		this.sourceType = SOURCE_TYPE;
	}

	public String getSourceType() {
		return SOURCE_TYPE;
	}

	public String getLicenseDate() {
		return licenseDate;
	}

	public void setLicenseDate(String licenseDate) {
		this.licenseDate = licenseDate;
	}

	public Date getFetchDate() {
		return fetchDate;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

	public Date getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(Date lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public Date getOfflineDate() {
		return offlineDate;
	}

	public void setOfflineDate(Date offlineDate) {
		this.offlineDate = offlineDate;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getStatusType() {
		return statusType;
	}

	public void setStatusType(Integer statusType) {
		this.statusType = statusType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRoadHaul() {
		return roadHaul;
	}

	public void setRoadHaul(String roadHaul) {
		this.roadHaul = roadHaul;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public String getGearbox() {
		return gearbox;
	}

	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}

	public String getContacter() {
		return contacter;
	}

	public void setContacter(String contacter) {
		this.contacter = contacter;
	}

	public String getContacterPhone() {
		return contacterPhone;
	}

	public void setContacterPhone(String contacterPhone) {
		this.contacterPhone = contacterPhone;
	}

	public int getSellerType() {
		return sellerType;
	}

	public void setSellerType(int sellerType) {
		this.sellerType = sellerType;
	}

	public String getCarSourceUrl() {
		return carSourceUrl;
	}

	public void setCarSourceUrl(String carSourceUrl) {
		this.carSourceUrl = carSourceUrl;
	}

	public String getSeqID() {
		return seqID;
	}

	public void setSeqID(String seqID) {
		this.seqID = seqID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDeclareDate() {
		return declareDate;
	}

	public void setDeclareDate(Date declareDate) {
		this.declareDate = declareDate;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	@Override
	public Auto51CarInfo clone() throws CloneNotSupportedException {
		return (Auto51CarInfo) super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Auto51CarInfo) {
			Auto51CarInfo ici = (Auto51CarInfo) obj;
			if (null != ici.getSeqID() && null != this.getSeqID()) {
				return ici.getSeqID().equals(this.getSeqID());
			}
		}
		return false;
	};

	@Override
	public String toString() {

		return new ToStringBuilder(this).append("url", this.getSourceType())
				.append("contacter phone", this.getContacterPhone())
				.append("contacter", this.getContacter()).toString();
	}

	public enum Auto51SellerType {
		SHOP(1), INDIVIDUAL(2);

		private int code;

		public int getCode() {
			return code;
		}

		private Auto51SellerType(int code) {
			this.code = code;
		}

		public static Auto51SellerType getByCode(int code) {
			for (Auto51SellerType iautosSellerType : Auto51SellerType.values()) {
				if (iautosSellerType.getCode() == code) {
					return iautosSellerType;
				}
			}
			throw new IllegalArgumentException(code
					+ " match non IautosSellerType");
		}

	}

	public enum Auto51StatusCode {
		STATUS_TYPE_FOR_SALE(1, "\u5F85\u552E"), STATUS_TYPE_SOLD(4,
				"\u5DF2\u552E");

		private int code;
		private String desc;

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

		public static Auto51StatusCode getByDesc(String desc) {
			if (null == desc) {
				throw new IllegalArgumentException(
						"IautosStatusCode desc is null");
			}
			for (Auto51StatusCode iautosStatusCode : Auto51StatusCode.values()) {
				if (iautosStatusCode.getDesc().equals(desc)) {
					return iautosStatusCode;
				}
			}
			throw new IllegalArgumentException(desc
					+ " match non IautosStatusCode");
		}

		public static Auto51StatusCode getByCode(int code) {
			for (Auto51StatusCode iautosStatusCode : Auto51StatusCode.values()) {
				if (iautosStatusCode.getCode() == code) {
					return iautosStatusCode;
				}
			}
			throw new IllegalArgumentException(code
					+ " match non IautosStatusCode");
		}

		private Auto51StatusCode(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
	}

}
