package cn.com.warlock.rest.filter;

import cn.com.warlock.rest.RequestHeader;

public class RequestHeaderHolder {

    private static ThreadLocal<RequestHeader> holder = new ThreadLocal<>();

    public static void set(RequestHeader rh) {
        holder.set(rh);
    }

    public static void clear() {
        holder.remove();
    }

    public static RequestHeader get() {
        RequestHeader header = holder.get();
        if (header == null) {
            header = new RequestHeader();
            holder.set(header);
        }
        return header;
    }
}
