package com.poly.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "Users")
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
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
	private boolean status;
	@Column(name = "Username")
	private String username;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles",
			joinColumns = {@JoinColumn(name = "user_id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id")})
	private Set<Role> roles;

	@CreationTimestamp
	@Column(name = "CreateTime", nullable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createTime;

	@UpdateTimestamp
	@Column(name = "ModifiedLastTime")
//	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime modifiedLastTime;
}
