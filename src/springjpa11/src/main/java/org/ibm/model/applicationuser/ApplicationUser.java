package org.ibm.model.applicationuser;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser {
	@Id
	private int id;
	private String name;
}
