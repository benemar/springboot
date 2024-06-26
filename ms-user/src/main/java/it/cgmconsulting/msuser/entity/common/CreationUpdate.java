package it.cgmconsulting.msuser.entity.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public class CreationUpdate extends Creation implements Serializable {

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
