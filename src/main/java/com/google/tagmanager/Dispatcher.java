package com.google.tagmanager;

import java.util.List;

interface Dispatcher {
    void close();

    void dispatchHits(List<Hit> list);

    boolean okToDispatch();
}
