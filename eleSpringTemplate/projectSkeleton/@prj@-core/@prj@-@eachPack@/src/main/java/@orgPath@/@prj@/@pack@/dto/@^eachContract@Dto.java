package @orgPath@.@prj@.@pack@.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;

public class @^entity@Dto {
	@<@
	@JsonProperty("@_eachAtt@")
	private @attType@ @eachAtt@;
	@>@

	@<@
	public @attType@ get@^eachAtt@() {
		return this.@eachAtt@;
	}
	public void set@^eachAtt@(@attType@ value) {
		this.@eachAtt@ = value;
	}
	@>@
}
