package se.vgregion.service.glasogonbidrag.util;

import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Lind
 */
public class SharedStringMethod {
    public static <T> String join(List<T> strings, String separator) {
        StringBuilder builder = new StringBuilder();

        Iterator<T> iterator = strings.iterator();
        if (iterator.hasNext()) builder.append(iterator.next());
        while (iterator.hasNext())
            builder.append(separator).append(iterator.next());

        return builder.toString();
    }
}
