package cn.com.warlock.common.packagescan;

/**
 * Compiles simple wildcard patterns
 */
public class SimpleWildcardPatternFactory implements PatternFactory {
    public CompiledPattern compile(String pattern) {
        return new SimpleWildcardPattern(pattern);
    }
}
