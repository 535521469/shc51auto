package pp.corleone.auto51.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SellerInfo_Auto51")
public class Auto51SellerInfo implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8254284734895259457L;

	@Id
	@GeneratedValue(generator = "uuidgenerator")
	@GenericGenerator(name = "uuidgenerator", strategy = "uuid")
	private String seqID;

	@Column(name = "SellerName", nullable = true)
	private String shopName;

	@Column(name = "SellerAddress", nullable = true)
	private String shopAddress;

	@Column(name = "SellerUrl", nullable = true)
	private String shopUrl;

	@Column(name = "MobilePhone", nullable = true)
	private String mobilePhone;

	@Column(name = "ContactPhone", nullable = true)
	private String contactPhone;

	@Column(name = "Contacter", nullable = true)
	private String contacter;

	public String getContacter() {
		return contacter;
	}

	public void setContacter(String contacter) {
		this.contacter = contacter;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@Column(name = "WireTelephone", nullable = true)
	private String wireTelephone;

	public String getWireTelephone() {
		return wireTelephone;
	}

	public void setWireTelephone(String wireTelephone) {
		this.wireTelephone = wireTelephone;
	}

	public String getSeqID() {
		return seqID;
	}

	public void setSeqID(String seqID) {
		this.seqID = seqID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String shopPhone) {
		this.mobilePhone = shopPhone;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("seqid", this.getSeqID())
				.append("phone", this.getMobilePhone())
				.append("address", this.getShopAddress())
				.append("name", this.getShopName()).toString();
	}
}