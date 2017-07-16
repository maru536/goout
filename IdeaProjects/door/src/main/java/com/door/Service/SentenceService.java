package com.door.Service;

import java.io.UnsupportedEncodingException;

/**
 * Created by wj on 17. 7. 1.
 */
public interface SentenceService {
    public String getSentence(String deviceId) throws UnsupportedEncodingException;
}
