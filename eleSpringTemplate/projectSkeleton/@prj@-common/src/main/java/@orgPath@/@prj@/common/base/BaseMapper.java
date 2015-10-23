package @orgPath@.@prj@.common.base;


import java.io.Serializable;
import java.util.List;

/**
 * @author kevin
 * @date 15/8/21
 */
public interface BaseMapper<E, PK extends Serializable> {

    public E getById(PK id);

    public Integer deleteById(PK id);

    /**
     * 插入数据
     */
    public Integer insert(E entity) ;

    /**
     * 更新数据
     */
    public Integer update(E entity) ;

    public Integer count(final BaseQuery query) ;

    public List<E> findList() ;

    public List<E> findList(final BaseQuery query) ;

    public List<E> findList(final BaseQuery query, Integer size) ;

    public List<E> findList(final BaseQuery query, Integer size, Integer offset) ;

    public Integer deleteByMultipleId(List<PK> idList) ;

    public List<E> getByMultipleId(List<PK> idList) ;
}
