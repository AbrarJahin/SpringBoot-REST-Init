package com.service;

import java.util.Date;

public class GlobalObjectRemoveMapper {
    public final Date creationTime;
    public String hash;

    public GlobalObjectRemoveMapper(String hash) {
        this.hash = hash;
        creationTime = new Date();
    }
}
