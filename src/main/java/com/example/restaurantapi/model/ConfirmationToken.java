package com.example.restaurantapi.model;

import com.example.restaurantapi.service.TableService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Table(name = "confirmationToken")
@Entity
@Data
@NoArgsConstructor
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String confirmationToken;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        this.confirmationToken = UUID.randomUUID().toString();
    }
}
