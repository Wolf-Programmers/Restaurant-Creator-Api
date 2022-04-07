package com.example.restaurantapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "item_type")
public class ItemType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="item_id")
    private List<Item> items;

    public static ItemType itemTypeWithoutItems(ItemType itemType) {
        ItemType dto = new ItemType();

        dto.setId(itemType.getId());
        dto.setName(itemType.getName());

        return dto;
    }
}
