package pp.corleone.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "HTTPProxy")
public class HttpProxy {

	@Id
	@GeneratedValue(generator = "uuidgenerator")
	@GenericGenerator(name = "uuidgenerator", strategy = "uuid")
	private String seqID;

	public HttpProxy() {
	}

	public HttpProxy(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	@Column(name = "Procotol", nullable = false)
	private String protocol;

	@Column(name = "IP", nullable = false)
	private String ip;

	@Column(name = "Port", nullable = false)
	private int port;

	@Column(name = "ValidDateTime", nullable = false)
	private Date validDateTime;

	@Column(name = "ValidFlag", nullable = false)
	private int validFlag;

	@Column(name = "FetchDate", nullable = false)
	private Date fetchDate;

	public String getSeqID() {
		return seqID;
	}

	public void setSeqID(String seqID) {
		this.seqID = seqID;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Date getValidDateTime() {
		return validDateTime;
	}

	public void setValidDateTime(Date validDateTime) {
		this.validDateTime = validDateTime;
	}

	public int getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(int validFlag) {
		this.validFlag = validFlag;
	}

	public Date getFetchDate() {
		return fetchDate;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

	public String expr() {
		return new StringBuilder(this.getIp()).append(":")
				.append(this.getPort()).toString();
	}

	public enum ValidFlagEnum {

		UnValid(0, "未验证"), Valid(1, "有效"), Invalid(2, "无效");

		private int code;
		private String desc;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		private ValidFlagEnum(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

	}

}
