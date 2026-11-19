import java.util.*;
public interface Dao<T,K>{
 int insert(T value)throws Exception;
 Optional<T> findById(K id)throws Exception;
 List<T> findAll()throws Exception;
 boolean update(T value)throws Exception;
 boolean delete(K id)throws Exception;
}