package com.poly.dto.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequest {
	private String name;
	private String description;
}
