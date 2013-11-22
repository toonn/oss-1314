package kuleuven.group6.policies;

import org.junit.runner.Request;

/**
 * This is an interface for policies that can have varying behaviour: sort,
 * filter, combine other policies...
 */
public interface IPolicy {
	/**
	 * @param request
	 *            A policy is applied to a Request.
	 * @return A Request that may differ from the original. (e.g.: sorted
	 *         differently, filtered...)
	 */
	public Request apply(Request request);

}
