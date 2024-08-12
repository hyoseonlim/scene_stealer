package pack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "followee_no")
    private User followee;

    @ManyToOne
    @JoinColumn(name = "follower_no")
    private User follower;

    public static Follow toEntity(Follow dto) {
    	return Follow.builder()
    			.no(dto.getNo())
    			.followee(dto.getFollowee())
    			.follower(dto.getFollower())
    			.build();
    }
    
}
