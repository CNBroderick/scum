package org.bklab.scum;

import dataq.core.jdbc.IRowMapper;

import java.sql.ResultSet;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ScumCode {
    private String type;
    private String code;
    private String enDescription;
    private String cnDescription;

    public String getType() {
        return type;
    }

    public ScumCode setType(String type) {
        this.type = type;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ScumCode setCode(String code) {
        this.code = code;
        return this;
    }

    public String getEnDescription() {
        return enDescription;
    }

    public ScumCode setEnDescription(String enDescription) {
        this.enDescription = enDescription;
        return this;
    }

    public String getCnDescription() {
        return cnDescription;
    }

    public ScumCode setCnDescription(String cnDescription) {
        this.cnDescription = cnDescription;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public static ScumCode parse(String[] s) {
        return s.length != 4 ? null
                : new ScumCode().setType(s[0].trim()).setCode(s[1].trim())
                .setEnDescription(s[2].trim()).setCnDescription(s[3].trim());
    }

    @SuppressWarnings("WeakerAccess")
    public static ScumCode parse(ResultSet r) {
        try {
            return new ScumCode().setType(r.getString("type"))
                    .setCode(r.getString("code"))
                    .setEnDescription(r.getString("en_description"))
                    .setCnDescription(r.getString("cn_description"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static IRowMapper getIRowMapper() {
        //noinspection unchecked,Anonymous2MethodRef
        return new IRowMapper() {
            @Override
            public ScumCode mapRow(ResultSet resultSet) {
                return ScumCode.parse(resultSet);
            }
        };
    }

    public Boolean isMatch(String s) {
        return s == null ? Boolean.TRUE :
                Pattern.matches(Stream.of(s.toLowerCase(Locale.CHINA).split(""))
                                .collect(Collectors.joining(".*", ".*", ".*")),
                        (type + code + enDescription + cnDescription).toLowerCase());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
