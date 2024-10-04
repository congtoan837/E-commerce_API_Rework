package com.poly.dto.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleRequest {
	private String name;
	private String description;
	private Set<String> permissions;
}
