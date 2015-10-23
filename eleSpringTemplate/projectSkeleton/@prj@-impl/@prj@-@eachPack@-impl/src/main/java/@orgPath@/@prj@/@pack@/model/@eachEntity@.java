package @orgPath@.@prj@.@pack@.model;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 */

public class @entity@ {
	
	private Long id;
	@<@
	private @attType@ @eachAtt@;
	@>@
	private int isDelete;

	private Timestamp createdAt;
	
	private Timestamp updatedAt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@<@
	public @attType@ get@^eachAtt@() {
		return this.@eachAtt@;
	}
	public void set@^eachAtt@(@attType@ value) {
		this.@eachAtt@ = value;
	}
	@>@
	
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}
