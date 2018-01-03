package com.blockchain.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by denis on 12/12/2017.
 */
public class StringUtil {

    public static String serializeToString(Object obj)  throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bo);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return bo.toString();
    }

}
