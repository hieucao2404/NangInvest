package model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Converter for User.Role enum to handle database storage as lowercase
 * strings
 */
@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<User.Role, String> {

  @Override
  public String convertToDatabaseColumn(User.Role role) {
    if (role == null) {
      return null;
    }
    return role.getDbValue();
  }

  @Override
  public User.Role convertToEntityAttribute(String dbValue) {
    if (dbValue == null || dbValue.trim().isEmpty()) {
      return null;
    }

    // Find the role by its database value
    for (User.Role role : User.Role.values()) {
      if (role.getDbValue().equals(dbValue)) {
        return role;
      }
    }

    // Fallback: try to match by enum name (for backwards compatibility)
    try {
      return User.Role.valueOf(dbValue.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unknown role value: " + dbValue);
    }
  }
}
