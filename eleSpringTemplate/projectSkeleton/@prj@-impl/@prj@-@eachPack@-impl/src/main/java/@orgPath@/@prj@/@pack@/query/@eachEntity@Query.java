package @orgPath@.@prj@.@pack@.query;

import @orgPath@.@prj@.common.base.BaseQuery;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.List;

public class @entity@Query extends BaseQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	/** id */
	private java.lang.Integer id;
	private java.lang.Integer idNotEq;
	private java.lang.Integer idGT;
	private java.lang.Integer idLT;

	private java.lang.Integer idGE;
	private java.lang.Integer idLE;

	private List<java.lang.Integer> idList;
	private List<java.lang.Integer> idNotEqList;

@<@
	/** @desc@ */
	private @attType@ @eachAtt@;
	private @attType@ @eachAtt@NotEq;
	private @attType@ @eachAtt@GT;
	private @attType@ @eachAtt@LT;

	private @attType@ @eachAtt@GE;
	private @attType@ @eachAtt@LE;

	private List<@attType@> @eachAtt@List;
	private List<@attType@> @eachAtt@NotEqList;
@>@
	

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getIdNotEq() {
		return idNotEq;
	}

	public void setIdNotEq(java.lang.Integer value) {
		this.idNotEq = value;
	}

	public java.lang.Integer getIdGT() {
		return idGT;
	}

	public void setIdGT(java.lang.Integer value) {
		this.idGT = value;
	}
	public java.lang.Integer getIdLT() {
		return idLT;
	}

	public void setIdLT(java.lang.Integer value) {
		this.idLT = value;
	}

	public java.lang.Integer getIdGE() {
		return idGE;
	}

	public void setIdGE(java.lang.Integer value) {
		this.idGE = value;
	}
	public java.lang.Integer getIdLE() {
		return idLE;
	}

	public void setIdLE(java.lang.Integer value) {
		this.idLE = value;
	}

	public List<java.lang.Integer> getIdList() {
		return this.idList;
	}

	public void setIdList(List<java.lang.Integer> value) {
		this.idList = value;
	}
	public List<java.lang.Integer> getIdNotEqList() {
		return idNotEqList;
	}

	public void setIdNotEqList(List<java.lang.Integer> value) {
		this.idNotEqList = value;
	}
	


@<@
	public @attType@ get@eachAtt@() {
		return this.@eachAtt@;
	}

	public void set@eachAtt@(@attType@ value) {
		this.@eachAtt@ = value;
	}
	public @attType@ get@eachAtt@NotEq() {
		return @eachAtt@NotEq;
	}

	public void set@eachAtt@NotEq(@attType@ value) {
		this.@eachAtt@NotEq = value;
	}

	public @attType@ get@eachAtt@GT() {
		return @eachAtt@GT;
	}

	public void set@eachAtt@GT(@attType@ value) {
		this.@eachAtt@GT = value;
	}
	public @attType@ get@eachAtt@LT() {
		return @eachAtt@LT;
	}

	public void set@eachAtt@LT(@attType@ value) {
		this.@eachAtt@LT = value;
	}

	public @attType@ get@eachAtt@GE() {
		return @eachAtt@GE;
	}

	public void set@eachAtt@GE(@attType@ value) {
		this.@eachAtt@GE = value;
	}
	public @attType@ get@eachAtt@LE() {
		return @eachAtt@LE;
	}

	public void set@eachAtt@LE(@attType@ value) {
		this.@eachAtt@LE = value;
	}

	public List<@attType@> get@eachAtt@List() {
		return this.@eachAtt@List;
	}

	public void set@eachAtt@List(List<@attType@> value) {
		this.@eachAtt@List = value;
	}
	public List<@attType@> get@eachAtt@NotEqList() {
		return @eachAtt@NotEqList;
	}

	public void set@eachAtt@NotEqList(List<@attType@> value) {
		this.@eachAtt@NotEqList = value;
	}
@>@



	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

}