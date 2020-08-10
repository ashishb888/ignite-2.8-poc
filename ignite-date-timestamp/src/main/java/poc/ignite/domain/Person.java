package poc.ignite.domain;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Person {
	@QuerySqlField(index = true)
	int id;
	@QuerySqlField
	String name;
	transient long l1;
	long l2;
	@QuerySqlField
	LocalDate ld;
	@QuerySqlField
	LocalDateTime ldt;
	@QuerySqlField
	Date d1;
	@QuerySqlField
	Date d2;
	@QuerySqlField
	Timestamp t1;
	@QuerySqlField
	Timestamp t2;

}
