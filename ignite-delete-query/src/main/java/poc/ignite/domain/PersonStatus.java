package poc.ignite.domain;

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
public class PersonStatus {
	@QuerySqlField(index = true)
	int id;
	@QuerySqlField
	int status; // 1: Active, 2: Mark for deletion, 3: Deleted
}
