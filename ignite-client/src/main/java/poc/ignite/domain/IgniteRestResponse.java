package poc.ignite.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IgniteRestResponse {
	private int successStatus;
	private String sessionToken;
	private String error;
	private String response;
}
