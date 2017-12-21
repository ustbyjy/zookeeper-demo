package com.ascend.subscribe;

public class ServerConfig {

    private String dbUrl;
    private String dbPwd;
    private String dbUser;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbPwd() {
        return dbPwd;
    }

    public void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServerConfig{");
        sb.append("dbUrl='").append(dbUrl).append('\'');
        sb.append(", dbPwd='").append(dbPwd).append('\'');
        sb.append(", dbUser='").append(dbUser).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
