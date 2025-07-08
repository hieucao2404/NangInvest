# JPA Entity Auto-Discovery Guide

## ✅ Auto-Discovery is Now Enabled!

Your `persistence.xml` is configured for automatic entity discovery. No need to manually list entity classes!

## How to Make Any Class a JPA Entity

### 1. Add Required Annotations

```java
import jakarta.persistence.*;

@Entity                           // Makes it a JPA entity
@Table(name = "your_table_name")  // Optional: specify table name
public class YourEntityClass {

    @Id                           // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "column_name") // Optional: specify column name
    private String someField;

    // Constructors, getters, setters...
}
```

### 2. Common JPA Annotations

| Annotation             | Purpose                  | Example                                                |
| ---------------------- | ------------------------ | ------------------------------------------------------ |
| `@Entity`              | Mark class as JPA entity | `@Entity`                                              |
| `@Table(name="users")` | Specify table name       | `@Table(name="users")`                                 |
| `@Id`                  | Primary key field        | `@Id`                                                  |
| `@GeneratedValue`      | Auto-generated values    | `@GeneratedValue(strategy = GenerationType.IDENTITY)`  |
| `@Column`              | Column mapping           | `@Column(name="user_name", length=50, nullable=false)` |
| `@Enumerated`          | Enum mapping             | `@Enumerated(EnumType.STRING)`                         |
| `@Temporal`            | Date/Time mapping        | `@Temporal(TemporalType.TIMESTAMP)`                    |

### 3. Entity Discovery Rules

✅ **Will be discovered:**

- Classes annotated with `@Entity`
- Located in your project's classpath
- Compiled in `target/classes`

❌ **Will NOT be discovered:**

- Classes without `@Entity` annotation
- Classes in excluded packages
- Abstract classes (unless specifically configured)

### 4. Your Current Entities Status

| Entity Class | Status             | Action Needed       |
| ------------ | ------------------ | ------------------- |
| `User`       | ✅ Configured      | Ready to use        |
| `Book`       | ❓ Needs `@Entity` | Add JPA annotations |
| `Course`     | ❓ Needs `@Entity` | Add JPA annotations |
| `Cart`       | ❓ Needs `@Entity` | Add JPA annotations |
| `Order`      | ❓ Needs `@Entity` | Add JPA annotations |
| `Service`    | ❓ Needs `@Entity` | Add JPA annotations |
| `Blog`       | ❓ Needs `@Entity` | Add JPA annotations |
| `Product`    | ❓ Needs `@Entity` | Add JPA annotations |
| Others...    | ❓ Needs `@Entity` | Add JPA annotations |

### 5. Next Steps

1. **Add JPA annotations to your existing model classes**
2. **Test each entity** as you add annotations
3. **No need to update persistence.xml** - auto-discovery handles everything!

### 6. Testing Your Entities

```java
// Test if an entity is properly configured
EntityManagerFactory emf = Persistence.createEntityManagerFactory("NangInvestPU");
EntityManager em = emf.createEntityManager();

// This will show if your entities are discovered
em.getMetamodel().getEntities().forEach(entityType -> {
    System.out.println("Discovered entity: " + entityType.getName());
});
```

## 🎯 Benefits of Auto-Discovery

- **Automatic**: New entities are included when you add `@Entity`
- **Clean Configuration**: No manual persistence.xml updates
- **Less Maintenance**: Fewer configuration files to manage
- **Flexible**: Works with any package structure
