package com.matyuhin.books.entity;

import com.matyuhin.books.models.ActionType;
import com.matyuhin.books.models.ObjectType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOGS")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "action_type")
    private ActionType actionType;

    @Column(name = "object_type")
    private ObjectType objectType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "object_name")
    private String objectName;

    @Column(name = "time")
    private String time;
}
