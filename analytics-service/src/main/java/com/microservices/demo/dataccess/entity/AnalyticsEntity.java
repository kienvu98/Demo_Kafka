package com.microservices.demo.dataccess.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "twitter_analytics")
public class AnalyticsEntity implements BaseEntity<UUID> {

    @Id
    @NotNull
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @Column(name = "word")
    private String word;

    @Column(name = "word_count")
    @NotNull
    private Long wordCount;

    @Column(name = "record_date")
    @NotNull
    private LocalDateTime recordDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalyticsEntity that = (AnalyticsEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
