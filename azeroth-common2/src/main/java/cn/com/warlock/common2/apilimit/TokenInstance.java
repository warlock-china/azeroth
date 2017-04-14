package cn.com.warlock.common2.apilimit;

public enum TokenInstance implements Token {

                                            USABLE() {
                                                public boolean isUsable() {
                                                    return true;
                                                }
                                            },

                                            UNUSABLE() {
                                                public boolean isUsable() {
                                                    return false;
                                                }
                                            };

}
