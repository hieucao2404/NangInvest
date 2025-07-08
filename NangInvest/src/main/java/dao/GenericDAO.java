package dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {

  // create
  T save(T entity);

  T saveAndFlush(T entity);

  // read
  Optional<T> findById(ID id);

  List<T> findAll();

  List<T> findAllById(List<ID> ids);

  long count();

  boolean existsById(ID id);

  // update
  T update(T entity);

  // delete

  void deleteById(ID id);

  void delete(T entity);

  void deleteAll();

  void deleteAll(List<T> entities);

  // pagination
  List<T> findAll(int page, int size);

  // Custom queries
  List<T> findByQuery(String jpql, Object... paramenters);

  Optional<T> findSingleByQuery(String jpql, Object... parameters);

  long countByQuery(String jpql, Object... parameters);

  // Batch operations
  void saveAll(List<T> entities);

  int executeUpdate(String jpql, Object... parameters);
}