package com.matyuhin.books.service;

import com.matyuhin.books.models.ActionType;
import com.matyuhin.books.models.ObjectType;

public interface LogService {
    void AddLog(ActionType actionType, ObjectType objectType, long objectId);
}
