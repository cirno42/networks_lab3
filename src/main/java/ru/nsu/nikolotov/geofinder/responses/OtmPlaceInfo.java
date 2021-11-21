package ru.nsu.nikolotov.geofinder.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = false)
public class OtmPlaceInfo {
    @Getter
    String xid;
    @Getter
    String name;
    @Getter
    Address address;
    @Getter
    OtmInfo info;

    public static class OtmInfo {
        @Getter
        String descr;
    }

    public String getDescription() {
        String desc = name + "\n" + address.getAddressString() + "\n";
        if (info != null) {
            desc += formattedDescr(info.getDescr());
        } else {
            desc += "No description was found for this place\n";
        }
        return desc;
    }
    private String formattedDescr(String descr) {
        if (descr == null) {
            return "No description was found for this place\n";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < descr.length(); i++) {
            stringBuilder.append(descr.charAt(i));
            if (descr.charAt(i) == '.') {
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }
}
