package com.example.restaurantapi.model;

import com.example.restaurantapi.dto.user.RegisterUserDto;
import com.example.restaurantapi.dto.user.UserInformationDto;
import com.example.restaurantapi.service.TableService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean locked = false;

    @Column(nullable = false)
    private boolean enabled = false;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    //Restaurant FK
    @OneToMany(mappedBy = "user_id")
    private List<Restaurant> restaurants;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "user")
    private List<Log> logs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.name());
        return Collections.singleton(simpleGrantedAuthority);
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /*
    Return correct user model for registration
     */
    public static User of(RegisterUserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhone_number());

        return user;
    }

    public static User updateUser(User user,UserInformationDto dto) {
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());

        return user;
    }



}
