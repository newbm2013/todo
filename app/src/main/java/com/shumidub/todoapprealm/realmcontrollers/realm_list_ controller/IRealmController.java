package com.shumidub.todoapprealm.realmcontrollers;

/**
 * Created by Артем on 28.01.2018.
 */

public interface IRealmController {

    static long getIdForNextValue() {
        return System.currentTimeMillis();
    }

}
