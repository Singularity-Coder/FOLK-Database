package com.singularitycoder.folkdatabase.helper;

public class AllCallbacks<T, K, V> {

    private T dataObject;
    private K status;
    private V message;

    public void set(T dataObject, K status, V message) {
        this.dataObject = dataObject;
        this.status = status;
        this.message = message;
    }

    public T getData() {
        return dataObject;
    }

    public K getStatus() {
        return status;
    }

    public V getMessage() {
        return message;
    }

}
