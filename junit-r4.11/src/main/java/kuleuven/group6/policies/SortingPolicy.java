package kuleuven.group6.policies;

import java.util.Comparator;

import org.junit.runner.Description;
import org.junit.runner.Request;

/**
 * 
 * @author Team 6
 *
 */
public abstract class SortingPolicy implements IPolicy {

	@Override
	public Request apply(Request request) {
		return request.sortWith(getComparator());
	}
	
	abstract protected Comparator<Description> getComparator();

}
