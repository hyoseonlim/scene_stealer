package pack.entity;

import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
import pack.dto.FollowDto;
import pack.dto.UserDto;

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

    public static FollowDto toDto(Follow entity) {
    	return FollowDto.builder() 
                .no(entity.getNo())
                .followees(entity.getFollowee().getFollowers().stream() 
                           .map(f -> f.getFollower().getNo()) 
                           .collect(Collectors.toList()))
                .followers(entity.getFollower().getFollowees().stream()
                           .map(f -> f.getFollowee().getNo())
                           .collect(Collectors.toList())) 
                .build();
    }
    
}
