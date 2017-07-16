package com.door.Config.type;

import lombok.Data;

@Data
public class URL {
    String url;

    public URL(String url_input) {
        url = url_input;
    }

    @Override
    public String toString() {
        return url.toString();
    }

    public void addParameter (String parameter, String value) {
        if(url.contains("?")) {
            url = url + "&" + parameter + "=" + value;
        }else{
            url = url + "?" + parameter + "=" + value;
        }
    }
}
