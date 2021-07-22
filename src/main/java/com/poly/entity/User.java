package com.poly.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "Users")
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "Name")
    private String name;
	@Column(name = "Email")
    private String email;
	@Column(name = "Phone")
    private String phone;
	@Column(name = "Address")
    private String address;
	@Column(name = "Password")
    private String password;
	@Column(name = "Image")
    private String image;
	@Column(name = "Status")
	private String status;
	@Column(name = "Username")
	private String username;
	@Column(name = "Role")
	private String role;
	@Column(name = "CreateTime")
	@Temporal(TemporalType.DATE)
	private Date createTime;
}
