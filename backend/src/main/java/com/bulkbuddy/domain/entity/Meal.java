package com.bulkbuddy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Standardized meal template managed by Admins.
 * <p>
 * Stores nutritional data per 100 g. Users select from these templates
 * when logging consumed meals — they cannot create custom meals.
 * </p>
 */
@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Meal name is required")
    @Column(nullable = false, length = 100)
    private String name;

    /** Calories per 100 g */
    @NotNull(message = "Calories value is required")
    @Min(value = 0, message = "Calories must be non-negative")
    @Column(nullable = false)
    private Integer calories;

    /** Protein in grams per 100 g */
    @NotNull(message = "Protein value is required")
    @Min(value = 0, message = "Protein must be non-negative")
    @Column(nullable = false)
    private Integer protein;

    /** Optional URL / path to a meal photo */
    @Column(length = 500)
    private String imageUrl;
}
