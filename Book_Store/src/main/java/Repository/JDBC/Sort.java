package Repository.JDBC;

import Domain.BaseEntity;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Sort<T extends BaseEntity<Long>> {
    List<String> args;
    Direction direction;

    public Sort(Direction direction, String ... args){
        this.direction = direction;
        this.args = Arrays.asList(args);
    }

    public Sort(String ... args){
        this.direction = Direction.ASC;
        this.args = Arrays.asList(args);
    }

    public List<T> sort(List<T> toSort){
        List<BeanComparator> comparers = args.stream().map(x-> new BeanComparator(x)).collect(Collectors.toList());
        ComparatorChain chain = new ComparatorChain(comparers);
        if(direction == Direction.DESC)
            args.stream().forEach(x->chain.setReverseSort(args.indexOf(x)));
        Collections.sort(toSort, chain);
        return toSort;
    }
}
