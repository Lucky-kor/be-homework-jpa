package com.springboot.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Stamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long stampId;


    @Column
    private int stampCount;

    public void setCoffeeStamp(int quantity){
        this.stampCount+=quantity;
    }

    @Column(nullable = false, updatable = false)
   private LocalDateTime createdAt= LocalDateTime.now();

    @Setter
    @Column(nullable = false, name="LAST_MODIFIED_AT")
   private LocalDateTime modifiedAt = LocalDateTime.now();

//    @OneToOne(mappedBy = "stamp",cascade = CascadeType.PERSIST)
//    private List<Member> members =new ArrayList<>();

}
