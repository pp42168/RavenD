package net.eai.umlmodel;

public class StarUmlReference {
	private String $ref;

	StarUmlReference()
	{
		
	}
	public StarUmlReference(String ref)
	{
		$ref = ref;
	}
	public String get$ref() {
		return $ref;
	}

	public void set$ref(String $ref) {
		this.$ref = $ref;
	}
}
