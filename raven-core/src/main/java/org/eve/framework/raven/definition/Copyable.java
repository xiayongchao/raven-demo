package org.eve.framework.raven.definition;

import org.eve.framework.raven.util.VerifyUtils;

import java.io.*;

/**
 * @author yc_xia
 * @date 2018/7/3
 */
public interface Copyable<T> {
    /**
     * 深拷贝操作，慎用，性能不咋地
     *
     * @param useSelf
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    default T deepCopy(boolean useSelf) throws IOException, ClassNotFoundException {
        if (useSelf) {
            return (T) this;
        }
        return this.deepCopy((T) this);
    }

    default T deepCopy(T source) throws IOException, ClassNotFoundException {
        if (VerifyUtils.isNull(source)) {
            return null;
        }
        // 序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(source);
        oos.close();
        // 反序列化
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        T object = (T) ois.readObject();
        ois.close();
        return object;
    }

}
