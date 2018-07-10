package org.eve.framework.raven.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author xiayc
 * @date 2018/7/10
 */
public class Collections {
    public static final void removeNullElement(Collection<?> collection) {
        if (VerifyUtils.isEmpty(collection)) {
            return;
        }
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (VerifyUtils.isNull(iterator.next())) {
                iterator.remove();
            }
        }
    }
}
