package ru.practicum.shareit.features.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validations.PatchValidationGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    @NotNull
    Long id;

    @NotBlank(groups = PatchValidationGroup.NameNotBlank.class)
    String name;
}
