package org.abstruck.miraibangumi.util;

import okhttp3.Response;

@FunctionalInterface
public interface Executor {
    void accept(Response response) throws Exception;
}
