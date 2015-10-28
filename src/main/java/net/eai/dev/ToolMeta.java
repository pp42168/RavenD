package net.eai.dev;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
public @interface ToolMeta {

	public String[] para() default {};

}
