package twitter.tools;

import java.io.Serializable;
import java.util.List;

import backtype.storm.tuple.Tuple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class RankableObjectWithFields implements Rankable, Serializable{

	private static final long serialVersionUID = 6509851064765238777L;

	private static final String toStringSeparator = "|";

	private final Object obj;
	private final long count;
	private final ImmutableList<Object> fields;

	public RankableObjectWithFields(Object obj, long count,
			Object... otherFields) {
		if (obj == null) {
			throw new IllegalArgumentException("The object must not be null");
		}
		if (count < 0) {
			throw new IllegalArgumentException("The count must be >= 0");
		}
		this.obj = obj;
		this.count = count;
		fields = ImmutableList.copyOf(otherFields);

	}

	/**
	 * Construct a new instance based on the provided {@link Tuple}.
	 * <p/>
	 * This method expects the object to be ranked in the first field (index 0)
	 * of the provided tuple, and the number of occurrences of the object (its
	 * count) in the second field (index 1). Any further fields in the tuple
	 * will be extracted and tracked, too. These fields can be accessed via
	 * {@link RankableObjectWithFields#getFields()}.
	 *
	 * @param tuple
	 *
	 * @return new instance based on the provided tuple
	 */
	public static RankableObjectWithFields from(Tuple tuple) {
		List<Object> otherFields = Lists.newArrayList(tuple.getValues());
		Object obj = otherFields.remove(0);
		Long count = (Long) otherFields.remove(0);
		return new RankableObjectWithFields(obj, count, otherFields.toArray());
	}

	public Object getObject() {
		return obj;
	}

	public long getCount() {
		return count;
	}

	/**
	 * @return an immutable list of any additional data fields of the object
	 *         (may be empty but will never be null)
	 */
	public List<Object> getFields() {
		return fields;
	}

	@Override
	public int compareTo(Rankable other) {
		long delta = this.getCount() - other.getCount();
		if (delta > 0) {
			return 1;
		} else if (delta < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RankableObjectWithFields)) {
			return false;
		}
		RankableObjectWithFields other = (RankableObjectWithFields) o;
		return obj.equals(other.obj) && count == other.count;
	}

	@Override
	public int hashCode() {
		int result = 17;
		int countHash = (int) (count ^ (count >>> 32));
		result = 31 * result + countHash;
		result = 31 * result + obj.hashCode();
		return result;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		buf.append(obj);
		buf.append(toStringSeparator);
		buf.append(count);
		for (Object field : fields) {
			buf.append(toStringSeparator);
			buf.append(field);
		}
		buf.append("]");
		return buf.toString();
	}

	@Override
	public Rankable copy() {
		List<Object> shallowCopyOfFields = ImmutableList.copyOf(getFields());
		return new RankableObjectWithFields(getObject(), getCount(),
				shallowCopyOfFields);
	}

}
