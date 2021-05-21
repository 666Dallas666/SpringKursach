package serv.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "deposits")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "userid")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Override
    public String toString() {
        return "Предмет=" + name  +
                ", цена=" + price;
    }
}
