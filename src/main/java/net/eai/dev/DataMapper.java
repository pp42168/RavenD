package net.eai.dev;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface DataMapper {


	@Select("desc ${table}")
	List<TableDesc> descTable(
			@Param("table") String table
			);


	@Select("show tables")
	List<String> showTables(
			);

}