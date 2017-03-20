package cn.com.warlock.common2.apilimit;

public interface RateLimiter extends Enablable {

    Token getToken(Key key);

    public int getAllowedRequests();

    public void setAllowedRequests(int allowedRequests);

    public void setDuration(int durationInSeconds);

    public int getDuration();

}
