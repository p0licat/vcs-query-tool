package org.ibm.model.applicationuser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="ApplicationUser")
@Table(name="ApplicationUsers")
public class ApplicationUser {
	@Id
	private int id;
	private String username;
}
