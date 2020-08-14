package poc.ignite.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Person implements Externalizable {
	@QuerySqlField(index = true)
	int id;
	@QuerySqlField
	String name;
	@QuerySqlField
	long l1;
	@QuerySqlField
	long l2;

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		id = in.readInt();
		l1 = in.readLong();
		l2 = in.readLong();
		name = (String) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(id);
		out.writeLong(l1);
		out.writeLong(l2);
		out.writeObject(name);
	}
}
