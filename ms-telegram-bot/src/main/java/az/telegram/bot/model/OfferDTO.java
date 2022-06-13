package az.telegram.bot.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferDTO {
    @Max(value = 9999999999L,message = "Price should be smaller than 9999999999")
    @Positive(message = "Price should be positive")
    Long price;
    @Size(max = 800, message = "Description should be less than 800 characters")
    @NotBlank(message = "Description must not be null or empty")
    String description;
    @Size(max = 200, message = "Notes should be less than 200 characters")
    @NotBlank(message = "Notes must not be null or empty")
    String notes;
    @NotBlank(message = "Date interim must not be null or empty")
    @Pattern(regexp = "^((0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).(19|20)\\d\\d-(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).(19|20)\\d\\d)$",
            message = "Invalid date interim!(Date should be like: 'xx.xx.xxxx-xx.xx.xxxx')")
    String dateInterim;
}